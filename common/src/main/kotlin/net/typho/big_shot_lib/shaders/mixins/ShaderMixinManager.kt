package net.typho.big_shot_lib.shaders.mixins

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.errors.SPVCException
import net.typho.big_shot_lib.errors.ShaderCompileException
import net.typho.big_shot_lib.platform.Services
import net.typho.big_shot_lib.shaders.ShaderType
import net.typho.big_shot_lib.shaders.mixins.builtin.BreezeWindShaderFix
import net.typho.big_shot_lib.shaders.mixins.builtin.ShaderLocationMapper
import net.typho.big_shot_lib.shaders.mixins.builtin.ShaderVersionUpdater
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memFree
import org.lwjgl.system.MemoryUtil.memUTF8
import org.lwjgl.util.shaderc.Shaderc.*
import org.lwjgl.util.spvc.Spvc.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object ShaderMixinManager {
    const val DEFAULT_ENTRYPOINT = "main"

    @JvmField
    @ApiStatus.Internal
    val currentVertexFormat = ThreadLocal<VertexFormat>()

    @JvmField
    @ApiStatus.Internal
    val currentLocationsInfo = ThreadLocal<ShaderLocationsInfo>()

    @JvmField
    val callbacks = LinkedList(
        listOf(
            BreezeWindShaderFix,
            ShaderVersionUpdater,
            ShaderLocationMapper
        )
    )

    @JvmField
    val compiler = shaderc_compiler_initialize()

    @JvmField
    val options = shaderc_compile_options_initialize()

    @JvmField
    val spvcContext: Long

    @JvmField
    val enabled = Services.PLATFORM.isFlagEnabled(BigShotLib.id("require_shader_mixins"))

    @JvmField
    val debug = Services.PLATFORM.isFlagEnabled(BigShotLib.id("debug_shader_mixins")) && Services.PLATFORM.isDevelopmentEnvironment()

    init {
        if (enabled) {
            BigShotLib.LOGGER.info("Enabling shader mixins")

            RenderSystem.recordRenderCall {
                if (!GL.getCapabilities().GL_ARB_gl_spirv) {
                    throw UnsupportedOperationException("Big Shot Lib Shader Mixins need opengl extension GL_ARB_gl_spirv")
                }
            }
        }

        shaderc_compile_options_set_target_env(options, shaderc_target_env_opengl, shaderc_env_version_opengl_4_5)
        shaderc_compile_options_set_source_language(options, shaderc_source_language_glsl)
        shaderc_compile_options_set_target_spirv(options, shaderc_spirv_version_1_0)
        shaderc_compile_options_set_auto_map_locations(options, true)
        shaderc_compile_options_set_auto_bind_uniforms(options, false)

        MemoryStack.stackPush().use { stack ->
            val pBuffer = stack.mallocPointer(1)
            spvc_context_create(pBuffer)
            spvcContext = pBuffer.get(0)
        }
    }

    @JvmStatic
    fun register(callback: ShaderMixinCallback) {
        if (!enabled) {
            throw IllegalStateException(
                "Big shot lib shader mixins are not enabled." +
                        "\nFor fabric, add \"big_shot_lib:require_shader_mixins\": true under the \"custom\" tag in your fabric.mod.json." +
                        "\nFor [neo]forge, add \"big_shot_lib:require_shader_mixins\": true under the \"modproperties\" tag under your mod definition in your mods.toml."
            )
        }

        callbacks.add(callback)
    }

    @JvmStatic
    fun apply(
        shader: ResourceLocation,
        type: ShaderType,
        format: VertexFormat?,
        locations: ShaderLocationsInfo,
        fileName: String,
        code: String,
        entrypoint: String = DEFAULT_ENTRYPOINT
    ): String {
        var modified = code.trim()

        for (callback in callbacks) {
            modified = callback.mixinPreCompile(shader, type, format, modified)
        }

        if (debug) {
            val path = Paths.get("shader_dump", "pre_spirv", shader.namespace, shader.path + "." + type.extension)
            Files.createDirectories(path.parent)
            Files.writeString(path, modified)
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
            throw ShaderCompileException(
                "SPIR-V compilation of $type shader for $fileName failed:\n${shaderc_result_get_error_message(result)?.trim()}"
            )
        }

        val bytes = shaderc_result_get_bytes(result)!!

        val context = ShaderMixinContext(bytes)

        for (callback in callbacks) {
            callback.mixinPostCompile(shader, type, format, context, locations)
        }

        if (debug) {
            val array = ByteArray(context.code.capacity()) { i -> context.code.get(i) }
            val path = Paths.get("shader_dump", "spirv", shader.namespace, shader.path + "." + type.extension + ".bin")
            Files.createDirectories(path.parent)
            Files.write(path, array)
        }

        val nativeBuffer = MemoryUtil.memAlloc(context.code.capacity())
            .put(context.code.rewind())
            .flip()

        MemoryStack.stackPush().use { stack ->
            val pParsed = stack.mallocPointer(1)
            val nativeInts = nativeBuffer.asIntBuffer()
            val eParsed = spvc_context_parse_spirv(
                spvcContext,
                nativeInts,
                nativeInts.capacity().toLong(),
                pParsed
            )

            if (eParsed != SPVC_SUCCESS) {
                throw SPVCException(eParsed, spvcContext)
            }

            val pCompiler = stack.mallocPointer(1)
            val eCompiler = spvc_context_create_compiler(
                spvcContext,
                SPVC_BACKEND_GLSL,
                pParsed.get(0),
                SPVC_CAPTURE_MODE_TAKE_OWNERSHIP,
                pCompiler
            )

            if (eCompiler != SPVC_SUCCESS) {
                throw SPVCException(eCompiler, spvcContext)
            }

            val pOptions = stack.mallocPointer(1)
            val eOptions = spvc_compiler_create_compiler_options(
                pCompiler.get(0),
                pOptions
            )

            if (eOptions != SPVC_SUCCESS) {
                throw SPVCException(eOptions, spvcContext)
            }

            val eOptions1 = spvc_compiler_options_set_uint(
                pOptions.get(0),
                SPVC_COMPILER_OPTION_GLSL_VERSION,
                450
            )

            if (eOptions1 != SPVC_SUCCESS) {
                throw SPVCException(eOptions1, spvcContext)
            }

            val eOptions2 = spvc_compiler_options_set_bool(
                pOptions.get(0),
                SPVC_COMPILER_OPTION_GLSL_ES,
                false
            )

            if (eOptions2 != SPVC_SUCCESS) {
                throw SPVCException(eOptions2, spvcContext)
            }

            val eOptions3 = spvc_compiler_options_set_bool(
                pOptions.get(0),
                SPVC_COMPILER_OPTION_GLSL_SEPARATE_SHADER_OBJECTS,
                false
            )

            if (eOptions3 != SPVC_SUCCESS) {
                throw SPVCException(eOptions3, spvcContext)
            }

            val eInstallOptions = spvc_compiler_install_compiler_options(
                pCompiler.get(0),
                pOptions.get(0)
            )

            if (eInstallOptions != SPVC_SUCCESS) {
                throw SPVCException(eInstallOptions, spvcContext)
            }

            val eEntryPoint = spvc_compiler_set_entry_point(
                pCompiler.get(0),
                entrypoint,
                type.spvcId
            )

            if (eEntryPoint != SPVC_SUCCESS) {
                throw SPVCException(eEntryPoint, spvcContext)
            }

            val pCompiled = stack.mallocPointer(1)
            val eCompiled = spvc_compiler_compile(
                pCompiler.get(0),
                pCompiled
            )

            if (eCompiled != SPVC_SUCCESS) {
                throw SPVCException(eCompiled, spvcContext)
            }

            memFree(nativeBuffer)
            val finalCode = memUTF8(pCompiled.get(0))

            if (debug) {
                val path = Paths.get("shader_dump", "post_spirv", shader.namespace, shader.path + "." + type.extension)
                Files.createDirectories(path.parent)
                Files.writeString(path, finalCode)
            }

            return finalCode
        }
    }
}