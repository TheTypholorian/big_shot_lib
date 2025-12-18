package net.typho.big_shot_lib.spirv

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.error.ShaderCompileException
import net.typho.big_shot_lib.gl.resource.ShaderType
import org.lwjgl.util.shaderc.Shaderc.*
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

interface ShaderMixinCallback {
    fun modify(shader: ResourceLocation, type: ShaderType, context: ShaderMixinContext)

    companion object {
        @JvmStatic
        val callbacks = LinkedList<ShaderMixinCallback>()
        @JvmStatic
        val compiler = shaderc_compiler_initialize()
        @JvmStatic
        val options = shaderc_compile_options_initialize()

        init {
            shaderc_compile_options_set_target_env(options, shaderc_target_env_opengl, shaderc_env_version_opengl_4_5)
            shaderc_compile_options_set_source_language(options, shaderc_source_language_glsl)
            shaderc_compile_options_set_auto_map_locations(options, true)
            shaderc_compile_options_set_auto_bind_uniforms(options, true)
            shaderc_compile_options_set_binding_base(options, shaderc_uniform_kind_buffer, 100)
            shaderc_compile_options_set_binding_base(options, shaderc_uniform_kind_sampler, 200)
            shaderc_compile_options_set_binding_base(options, shaderc_uniform_kind_image, 300)
            shaderc_compile_options_set_binding_base(options, shaderc_uniform_kind_storage_buffer, 400)
            shaderc_compile_options_set_binding_base(options, shaderc_uniform_kind_texture, 500)
        }

        @JvmStatic
        fun register(callback: ShaderMixinCallback) {
        }

        @JvmStatic
        fun compile(type: ShaderType, fileName: String, code: String, entrypoint: String = "main"): ByteBuffer {
            val result = shaderc_compile_into_spv(
                compiler,
                code,
                type.shadercId,
                fileName,
                entrypoint,
                options
            )

            val status = shaderc_result_get_compilation_status(result)
            if (status != shaderc_compilation_status_success) {
                throw ShaderCompileException("SPIR-V compilation of $type shader for $fileName failed:\n${shaderc_result_get_error_message(result)?.trim()}")
            }

            val bytes = shaderc_result_get_bytes(result)!!

            val array = ByteArray(bytes.capacity())
            bytes.get(0, array)
            Files.write(Paths.get("shader_dump", "$fileName.bin"), array)

            return bytes
        }

        @JvmStatic
        fun invoke(shader: ResourceLocation, type: ShaderType, compiled: ByteBuffer): ByteBuffer {
            val context = ShaderMixinContext()
            context.code.add(compiled.order(ShaderMixinContext.BYTE_ORDER))
            context.loadBound()

            for (callback in callbacks) {
                callback.modify(shader, type, context)
            }

            return context.compile()
        }
    }
}