package net.typho.big_shot_lib.spirv

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.error.ShaderCompileException
import net.typho.big_shot_lib.gl.resource.ShaderType
import net.typho.big_shot_lib.platform.Services
import net.typho.big_shot_lib.spirv.mixin.BreezeWindShaderFix
import net.typho.big_shot_lib.spirv.mixin.ShaderLocationMapper
import net.typho.big_shot_lib.spirv.mixin.ShaderVersionUpdater
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.util.shaderc.Shaderc.*
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

interface ShaderMixinCallback {
    fun mixinGLSL(shader: ResourceLocation, type: ShaderType, format: VertexFormat?, code: String): String = code

    fun mixinSpirV(shader: ResourceLocation, type: ShaderType, format: VertexFormat?, context: ShaderMixinContext, locations: ShaderLocationsInfo) {
    }

    companion object {
        @JvmField
        @ApiStatus.Internal
        val currentVertexFormat = ThreadLocal<VertexFormat>()
        @JvmField
        @ApiStatus.Internal
        val currentLocationsInfo = ThreadLocal<ShaderLocationsInfo>()

        @JvmField
        val callbacks = LinkedList<ShaderMixinCallback>()

        @JvmField
        val compiler = shaderc_compiler_initialize()
        @JvmField
        val options = shaderc_compile_options_initialize()

        init {
            shaderc_compile_options_set_target_env(options, shaderc_target_env_opengl, shaderc_env_version_opengl_4_5)
            shaderc_compile_options_set_source_language(options, shaderc_source_language_glsl)
            shaderc_compile_options_set_target_spirv(options, shaderc_spirv_version_1_5)

            shaderc_compile_options_set_auto_map_locations(options, true)
            shaderc_compile_options_set_auto_bind_uniforms(options, false)

            register(BreezeWindShaderFix)
            register(ShaderVersionUpdater)
            register(ShaderLocationMapper)
        }

        @JvmStatic
        fun register(callback: ShaderMixinCallback) {
            callbacks.add(callback)
        }

        @JvmStatic
        fun compile(shader: ResourceLocation, type: ShaderType, format: VertexFormat?, locations: ShaderLocationsInfo, fileName: String, code: String, entrypoint: String = "main"): ByteBuffer {
            var modified = code

            for (callback in callbacks) {
                modified = callback.mixinGLSL(shader, type, format, modified)
            }

            val result = shaderc_compile_into_spv(
                compiler,
                modified,
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

            val context = ShaderMixinContext(bytes)

            for (callback in callbacks) {
                callback.mixinSpirV(shader, type, format, context, locations)
            }

            if (Services.PLATFORM.isDevelopmentEnvironment()) {
                val array = ByteArray(context.code.capacity()) { i -> context.code.get(i) }
                val path = Paths.get("shader_dump", shader.namespace, shader.path + "." + type.extension + ".bin")
                Files.createDirectories(path.parent)
                Files.write(path, array)
            }

            return context.code
        }
    }
}