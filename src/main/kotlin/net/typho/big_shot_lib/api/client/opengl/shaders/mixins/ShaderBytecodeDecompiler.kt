package net.typho.big_shot_lib.api.client.opengl.shaders.mixins

import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.errors.ShaderDecompileException
import org.lwjgl.PointerBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.memUTF8
import org.lwjgl.system.NativeResource
import org.lwjgl.util.spvc.Spvc.*
import java.util.function.Function
import java.util.function.Supplier

open class ShaderBytecodeDecompiler(
    @JvmField
    val context: Long = MemoryStack.stackPush().use { stack ->
        val pBuffer = stack.mallocPointer(1)
        spvc_context_create(pBuffer)
        pBuffer.get(0)
    }
) : NativeResource {
    fun decompile(
        key: ShaderSourceKey,
        code: ShaderBytecodeBuffer,
        entrypoint: String = "main"
    ): String {
        MemoryStack.stackPush().use { stack ->
            fun spvcRun(op: Supplier<Int>) {
                if (op.get() != SPVC_SUCCESS) {
                    throw ShaderDecompileException("SPIR-V decompilation of ${key.type.name.lowercase()} shader of ${key.program.location} failed:\n${spvc_context_get_last_error_string(context)}")
                }
            }

            fun spvcGet(op: Function<PointerBuffer, Int>): Long {
                val pointer = stack.mallocPointer(1)
                spvcRun { op.apply(pointer) }
                return pointer.get(0)
            }

            val buffer = code.ensureDirect().clear()
            val parsed = spvcGet { spvc_context_parse_spirv(context, buffer, buffer.capacity().toLong(), it) }
            val spvcCompiler = spvcGet { spvc_context_create_compiler(context, SPVC_BACKEND_GLSL, parsed, SPVC_CAPTURE_MODE_TAKE_OWNERSHIP, it) }
            val spvcOptions = spvcGet { spvc_compiler_create_compiler_options(spvcCompiler, it) }

            spvcRun { spvc_compiler_options_set_uint(spvcOptions, SPVC_COMPILER_OPTION_GLSL_VERSION, 450) }
            spvcRun { spvc_compiler_options_set_bool(spvcOptions, SPVC_COMPILER_OPTION_GLSL_ES, false) }
            spvcRun { spvc_compiler_options_set_bool(spvcOptions, SPVC_COMPILER_OPTION_GLSL_SEPARATE_SHADER_OBJECTS, false) }

            spvcRun { spvc_compiler_install_compiler_options(spvcCompiler, spvcOptions) }
            spvcRun { spvc_compiler_set_entry_point(spvcCompiler, entrypoint, key.type.spvcId!!) }

            val compiled = spvcGet { spvc_compiler_compile(spvcCompiler, it) }
            return memUTF8(compiled)
        }
    }

    override fun free() {
        spvc_context_destroy(context)
    }
}