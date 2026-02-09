package net.typho.big_shot_lib.api.shaders.mixins

import net.typho.big_shot_lib.api.errors.ShaderCompileException
import net.typho.big_shot_lib.api.errors.ShaderDecompileException
import net.typho.big_shot_lib.api.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.shaders.ShaderSourceType
import org.lwjgl.PointerBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.memFree
import org.lwjgl.system.MemoryUtil.memUTF8
import org.lwjgl.util.shaderc.Shaderc.*
import org.lwjgl.util.spvc.Spvc.*
import java.nio.ByteOrder
import java.util.*
import java.util.function.Function
import java.util.function.Supplier

object ShaderMixinManager {
    const val DEFAULT_ENTRYPOINT = "main"
    const val WORD_SIZE_BYTES = 4
    @JvmField
    val BYTE_ORDER: ByteOrder = ByteOrder.nativeOrder()
    @JvmField
    val shadercContext = shaderc_compiler_initialize()
    @JvmField
    val shadercOptions = shaderc_compile_options_initialize()
    @JvmField
    val spvcContext: Long

    init {
        shaderc_compile_options_set_target_env(shadercOptions, shaderc_target_env_opengl, shaderc_env_version_opengl_4_5)
        shaderc_compile_options_set_source_language(shadercOptions, shaderc_source_language_glsl)
        shaderc_compile_options_set_target_spirv(shadercOptions, shaderc_spirv_version_1_0)
        shaderc_compile_options_set_auto_map_locations(shadercOptions, true)
        shaderc_compile_options_set_auto_bind_uniforms(shadercOptions, false)

        MemoryStack.stackPush().use { stack ->
            val pBuffer = stack.mallocPointer(1)
            spvc_context_create(pBuffer)
            spvcContext = pBuffer.get(0)
        }
    }

    private var mixins = LinkedList(listOf(
        ShaderMixin.Factory { ShaderVersionUpdaterMixin },
        ShaderLocationMapperMixin
    ))

    @JvmStatic
    fun register(mixin: ShaderMixin.Factory) {
        if (mixins.contains(mixin)) {
            throw IllegalStateException()
        }

        mixins.add(mixin)
    }

    @JvmStatic
    fun register(mixin: ShaderMixin) = register { mixin }

    @JvmStatic
    fun create(key: ShaderProgramKey): Instance {
        return Instance(key)
    }

    class Instance(
        @JvmField
        val key: ShaderProgramKey
    ) {
        private val mixins = LinkedList<Pair<ShaderMixin.Factory, ShaderMixin?>>()

        init {
            for (factory in this@ShaderMixinManager.mixins) {
                getOrCreateMixinInstance(factory)
            }
        }

        fun apply(type: ShaderSourceType, code: String): String {
            return applyImpl(
                ShaderSourceKey(key, type),
                code,
                mixins.mapNotNull { it.second }
            )
        }

        fun getOrCreateMixinInstance(factory: ShaderMixin.Factory): ShaderMixin? {
            for (pair in mixins) {
                if (pair.first == factory) {
                    return pair.second
                }
            }

            if (this@ShaderMixinManager.mixins.contains(factory)) {
                val mixin = factory.create(key)
                mixins.add(factory to mixin)
                return mixin
            }

            return null
        }
    }

    @JvmStatic
    fun applyImpl(
        key: ShaderSourceKey,
        code: String,
        mixins: Collection<ShaderMixin>,

        shadercContext: Long = this.shadercContext,
        shadercOptions: Long = this.shadercOptions,

        spvcContext: Long = this.spvcContext,

        entrypoint: String = DEFAULT_ENTRYPOINT
    ): String {
        if (mixins.isEmpty()) {
            return code
        }

        val compileResult = shaderc_compile_into_spv(
            shadercContext,
            mixins.fold(code.trim()) { code, mixin -> mixin.mixinPreCompile(key, code) },
            key.type.shadercId,
            key.fileName(),
            entrypoint,
            shadercOptions
        )

        if (shaderc_result_get_compilation_status(compileResult) != shaderc_compilation_status_success) {
            throw ShaderCompileException(
                "SPIR-V compilation of ${key.type.name.lowercase()} shader of ${key.program.location} failed:\n${
                    shaderc_result_get_error_message(compileResult)?.trim()
                }"
            )
        }

        val nativeBytecodeBuffer = shaderc_result_get_bytes(compileResult)!!
        val bytecodeBuffer = mixins.fold(ShaderBytecodeBuffer(nativeBytecodeBuffer)) { buffer, mixin -> mixin.mixinBytecode(key, buffer) }

        MemoryStack.stackPush().use { stack ->
            fun spvcRun(op: Supplier<Int>) {
                if (op.get() != SPVC_SUCCESS) {
                    throw ShaderDecompileException(
                        "SPIR-V decompilation of ${key.type.name.lowercase()} shader of ${key.program.location} failed:\n${
                            spvc_context_get_last_error_string(spvcContext)
                        }"
                    )
                }
            }

            fun spvcGet(op: Function<PointerBuffer, Int>): Long {
                val pointer = stack.mallocPointer(1)
                spvcRun { op.apply(pointer) }
                return pointer.get(0)
            }

            val parsed = spvcGet {
                val buffer = bytecodeBuffer.ensureDirect().clear()
                spvc_context_parse_spirv(spvcContext, buffer, buffer.capacity().toLong(), it)
            }
            val spvcCompiler = spvcGet { spvc_context_create_compiler(spvcContext, SPVC_BACKEND_GLSL, parsed, SPVC_CAPTURE_MODE_TAKE_OWNERSHIP, it) }
            val spvcOptions = spvcGet { spvc_compiler_create_compiler_options(spvcCompiler, it) }

            spvcRun { spvc_compiler_options_set_uint(spvcOptions, SPVC_COMPILER_OPTION_GLSL_VERSION, 450) }
            spvcRun { spvc_compiler_options_set_bool(spvcOptions, SPVC_COMPILER_OPTION_GLSL_ES, false) }
            spvcRun { spvc_compiler_options_set_bool(spvcOptions, SPVC_COMPILER_OPTION_GLSL_SEPARATE_SHADER_OBJECTS, false) }

            spvcRun { spvc_compiler_install_compiler_options(spvcCompiler, spvcOptions) }
            spvcRun { spvc_compiler_set_entry_point(spvcCompiler, entrypoint, key.type.spvcId!!) }

            val compiled = spvcGet { spvc_compiler_compile(spvcCompiler, it) }

            memFree(nativeBytecodeBuffer)
            return mixins.fold(memUTF8(compiled)) { code, mixin -> mixin.mixinPostCompile(key, code) }
        }
    }
}