package net.typho.big_shot_lib.spirv

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.error.ShaderCompileException
import net.typho.big_shot_lib.gl.resource.ShaderType
import org.lwjgl.util.shaderc.Shaderc.*
import java.nio.ByteBuffer
import java.util.*

interface ShaderMixinCallback {
    fun mixinGLSL(shader: ResourceLocation, type: ShaderType, code: String): String = code

    fun mixinSpirV(shader: ResourceLocation, type: ShaderType, context: ShaderMixinContext) {
    }

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
            shaderc_compile_options_set_target_spirv(options, shaderc_spirv_version_1_5)

            shaderc_compile_options_set_auto_map_locations(options, true)
            shaderc_compile_options_set_auto_bind_uniforms(options, false)

            register(object : ShaderMixinCallback {
                override fun mixinGLSL(shader: ResourceLocation, type: ShaderType, code: String): String {
                    if (code.startsWith("#version")) {
                        val version =
                            code.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()

                        if (version < 450) {
                            return "#version 450 core" + code.substring(code.indexOf('\n'))
                        }
                    } else {
                        return "#version 450 core\n$code"
                    }

                    return code
                }
            })

            register(object : ShaderMixinCallback {
                override fun mixinGLSL(shader: ResourceLocation, type: ShaderType, code: String): String {
                    when (shader.toString()) {
                        "minecraft:rendertype_translucent_moving_block" -> {
                            return code.replace("uniform sampler2D Sampler2;", "layout(location = 1000) uniform sampler2D Sampler2;")
                        }
                        "minecraft:rendertype_beacon_beam" -> {
                            return code.replace("uniform mat4 ProjMat;", "layout(location = 1000) uniform mat4 ProjMat;")
                        }
                        "minecraft:rendertype_breeze_wind" -> {
                            return code.replace("uniform sampler2D Sampler0;", "layout(location = 1000) uniform sampler2D Sampler0;")
                                .replace("out vec4 lightMapColor;", "layout(location = 1000) out vec4 lightMapColor;")
                        }
                    }

                    return code
                }

                override fun mixinSpirV(
                    shader: ResourceLocation,
                    type: ShaderType,
                    context: ShaderMixinContext
                ) {
                    if (type != ShaderType.VERTEX) {
                        val code = context.compile()
                        val offset = type.ordinal * 100

                        mainLoop@
                        for (opcode in context) {
                            if (opcode.type == 71) { // OpDecorate
                                val id = code.getInt((opcode.index + 1) * ShaderMixinContext.WORD_SIZE_BYTES)

                                for (opcode1 in context) {
                                    if (opcode1.type == 59) { // OpVariable
                                        val checkId = code.getInt((opcode1.index + 2) * ShaderMixinContext.WORD_SIZE_BYTES)

                                        if (checkId == id) {
                                            val storageClass = code.getInt((opcode1.index + 3) * ShaderMixinContext.WORD_SIZE_BYTES)

                                            if (storageClass == 0) { // UniformConstant
                                                break
                                            } else {
                                                continue@mainLoop
                                            }
                                        }
                                    }
                                }

                                val decoration = code.getInt((opcode.index + 2) * ShaderMixinContext.WORD_SIZE_BYTES)

                                if (decoration == 30) { // Location
                                    val index = (opcode.index + 3) * ShaderMixinContext.WORD_SIZE_BYTES
                                    val location = code.getInt(index)

                                    if (location < 1000) {
                                        code.putInt(index, location + offset)
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }

        @JvmStatic
        fun register(callback: ShaderMixinCallback) {
            callbacks.add(callback)
        }

        @JvmStatic
        fun compile(shader: ResourceLocation, type: ShaderType, fileName: String, code: String, entrypoint: String = "main"): ByteBuffer {
            var modified = code

            for (callback in callbacks) {
                modified = callback.mixinGLSL(shader, type, modified)
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

            val bytes = shaderc_result_get_bytes(result)!!//ByteBuffer.allocate(shaderc_result_get_length(result).toInt())
            //MemoryUtil.memCopy(shaderc_result_get_bytes(result)!!, bytes)
            //bytes.flip()

            //shaderc_result_release(result)

            val context = ShaderMixinContext()
            context.code.add(bytes.order(ShaderMixinContext.BYTE_ORDER))
            context.loadBound()

            for (callback in callbacks) {
                callback.mixinSpirV(shader, type, context)
            }

            return context.compile()
        }
    }
}