package net.typho.big_shot_lib.api

import com.google.gson.JsonElement
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.platform.Window
import com.mojang.blaze3d.shaders.AbstractUniform
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.impl.NeoShader
import net.typho.big_shot_lib.gl.Bindable
import net.typho.big_shot_lib.gl.resource.GlResourceInstance
import net.typho.big_shot_lib.spirv.ShaderExtension
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW
import java.io.InputStream
import java.util.*

interface IShader : Bindable, GlResourceInstance, ShaderExtension {
    fun getUniform(name: String): AbstractUniform?

    fun setSampler(name: String, id: Int)

    fun setSampler(name: String, target: RenderTarget) = setSampler(name, target.colorTextureId)

    fun setSampler(name: String, texture: AbstractTexture) = setSampler(name, texture.id)

    fun setSampler(name: String, texture: ITexture) = setSampler(name, texture.id())

    fun setCommonUniforms(
        time: Float = GLFW.glfwGetTime().toFloat(),
        window: Window = Minecraft.getInstance().window,
        projMat: Matrix4f = RenderSystem.getProjectionMatrix(),
        modelViewMat: Matrix4f = RenderSystem.getModelViewMatrix()
    ) {
        getUniform("Time")?.set(time)
        getUniform("ScreenSize")?.set(window.width.toFloat(), window.height.toFloat())
        getUniform("ProjMat")?.set(projMat)
        getUniform("ModelViewMat")?.set(modelViewMat)
    }

    fun vertexFormat(): VertexFormat?

    companion object {
        @JvmStatic
        fun loadInclude(location: ResourceLocation): ByteArray {
            val resource = Minecraft.getInstance().resourceManager.getResourceOrThrow(
                location.withPath { path ->
                    if (path.contains('.')) {
                        return@withPath "${NeoShader.PATH}/$path"
                    } else {
                        return@withPath "${NeoShader.PATH}/$path.glsl"
                    }
                }
            )
            return resource.open().use(InputStream::readAllBytes)
        }

        @JvmStatic
        fun resolveIncludes(source: String): String {
            var newSource = source
            var includeString = "#include \""
            var i = 0

            while (i < newSource.length) {
                if (newSource.startsWith(includeString, i)) {
                    val start = i + includeString.length
                    val end = newSource.indexOf('"', start)
                    val location = ResourceLocation.parse(newSource.substring(start, end))
                    val code = String(loadInclude(location))
                    newSource = newSource.substring(0, i) + code + newSource.substring(end + 1)
                    i = 0
                }

                includeString = "\n#include \""
                i++
            }

            return newSource
        }

        @JvmStatic
        fun parseFormat(json: JsonElement?): VertexFormat? {
            if (json == null) {
                return null
            }

            if (json.isJsonArray) {
                val array = json.getAsJsonArray()
                val builder = VertexFormat.builder()

                for (element in array) {
                    val s = element.asString

                    builder.add(
                        s, when (s) {
                            "Position" -> VertexFormatElement.POSITION
                            "Color" -> VertexFormatElement.COLOR
                            "UV0", "UV_0" -> VertexFormatElement.UV0
                            "UV1", "UV_1" -> VertexFormatElement.UV1
                            "UV2", "UV_2" -> VertexFormatElement.UV2
                            "Normal" -> VertexFormatElement.NORMAL
                            else -> throw IllegalStateException("Invalid vertex format element $s")
                        }
                    )
                }

                return builder.build()
            } else {
                val name = json.asString

                return when (name.uppercase(Locale.getDefault())) {
                    "POSITION_COLOR_TEXTURE_LIGHT_NORMAL", "BLOCK" -> DefaultVertexFormat.BLOCK
                    "POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL", "NEW_ENTITY" -> DefaultVertexFormat.NEW_ENTITY
                    "POSITION_TEXTURE_COLOR_LIGHT", "PARTICLE" -> DefaultVertexFormat.PARTICLE
                    "POSITION" -> DefaultVertexFormat.POSITION
                    "BLIT_SCREEN" -> DefaultVertexFormat.BLIT_SCREEN
                    "POSITION_COLOR" -> DefaultVertexFormat.POSITION_COLOR
                    "LINES", "POSITION_COLOR_NORMAL" -> DefaultVertexFormat.POSITION_COLOR_NORMAL
                    "POSITION_COLOR_LIGHT", "POSITION_COLOR_LIGHTMAP" -> DefaultVertexFormat.POSITION_COLOR_LIGHTMAP
                    "POSITION_TEXTURE", "POSITION_TEX" -> DefaultVertexFormat.POSITION_TEX
                    "POSITION_TEXTURE_COLOR", "POSITION_TEX_COLOR" -> DefaultVertexFormat.POSITION_TEX_COLOR
                    "POSITION_COLOR_TEXTURE_LIGHT", "POSITION_COLOR_TEX_LIGHTMAP" -> DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP
                    "POSITION_TEXTURE_LIGHT_COLOR", "POSITION_TEX_LIGHTMAP_COLOR" -> DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR
                    "POSITION_TEXTURE_COLOR_NORMAL", "POSITION_TEX_COLOR_NORMAL" -> DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL
                    else -> throw IllegalStateException("Invalid vertex format $name")
                }
            }
        }
    }
}