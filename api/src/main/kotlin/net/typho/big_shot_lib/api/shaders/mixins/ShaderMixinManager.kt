package net.typho.big_shot_lib.api.shaders.mixins

import net.typho.big_shot_lib.api.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.shaders.errors.ShaderCompilationException
import net.typho.big_shot_lib.api.shaders.errors.ShaderDecompilationException
import org.jetbrains.annotations.ApiStatus
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

interface ShaderMixinManager {
    fun registerMixin(mixin: ShaderMixin.Factory)

    fun registerMixin(mixin: ShaderMixin) = registerMixin { mixin }

    fun applyMixins(key: ShaderSourceKey, code: String): String

    companion object : ShaderMixinManager {
        private val INSTANCE = ServiceLoader.load(ShaderMixinManager::class.java).findFirst().orElseThrow()
        const val DEFAULT_ENTRYPOINT = "main"
        const val WORD_SIZE_BYTES = 4
        val BYTE_ORDER: ByteOrder = ByteOrder.nativeOrder()

        override fun registerMixin(mixin: ShaderMixin.Factory) = INSTANCE.registerMixin(mixin)

        override fun registerMixin(mixin: ShaderMixin) = INSTANCE.registerMixin(mixin)

        override fun applyMixins(key: ShaderSourceKey, code: String) = INSTANCE.applyMixins(key, code)

        @ApiStatus.Internal
        fun applyMixinsInternal(
            key: ShaderSourceKey,
            code: String,
            mixins: Iterable<ShaderMixin>,

            shadercContext: Long,
            shadercOptions: Long,

            spvcContext: Long,

            entrypoint: String = DEFAULT_ENTRYPOINT
        ): String {
            val compileResult = shaderc_compile_into_spv(
                shadercContext,
                mixins.fold(code.trim()) { code, mixin -> mixin.mixinPreCompile(key, code) },
                key.type.shadercId,
                key.fileName(),
                entrypoint,
                shadercOptions
            )

            if (shaderc_result_get_compilation_status(compileResult) != shaderc_compilation_status_success) {
                throw ShaderCompilationException(
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
                        throw ShaderDecompilationException(
                            "SPVC decompilation of ${key.type.name.lowercase()} shader of ${key.program.location} failed:\n${
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
                    val buffer = bytecodeBuffer.ensureDirect()
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
}