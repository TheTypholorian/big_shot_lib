package net.typho.big_shot_lib.api.client.opengl.shaders.mixins

import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.errors.ShaderCompileException
import org.lwjgl.system.NativeResource
import org.lwjgl.util.shaderc.Shaderc.*

open class ShaderBytecodeCompiler(
    @JvmField
    val context: Long = shaderc_compiler_initialize(),
    @JvmField
    val options: Long = shaderc_compile_options_initialize()
) : NativeResource {
    init {
        shaderc_compile_options_set_target_env(options, shaderc_target_env_opengl, shaderc_env_version_opengl_4_5)
        shaderc_compile_options_set_source_language(options, shaderc_source_language_glsl)
        shaderc_compile_options_set_target_spirv(options, shaderc_spirv_version_1_0)
        shaderc_compile_options_set_auto_map_locations(options, true)
        shaderc_compile_options_set_auto_bind_uniforms(options, true)
    }

    fun compile(
        key: ShaderSourceKey,
        code: String,
        entrypoint: String = "main"
    ): ShaderBytecodeBuffer {
        val compileResult = shaderc_compile_into_spv(
            context,
            code,
            key.type.shadercId,
            key.fileName(),
            entrypoint,
            options
        )

        if (shaderc_result_get_compilation_status(compileResult) != shaderc_compilation_status_success) {
            throw ShaderCompileException("SPIR-V compilation of ${key.type.name.lowercase()} shader of ${key.program.location} failed:\n${shaderc_result_get_error_message(compileResult)?.trim()}")
        }

        return ShaderBytecodeBuffer(shaderc_result_get_bytes(compileResult)!!)
    }

    override fun free() {
        shaderc_compile_options_release(options)
        shaderc_compiler_release(context)
    }
}