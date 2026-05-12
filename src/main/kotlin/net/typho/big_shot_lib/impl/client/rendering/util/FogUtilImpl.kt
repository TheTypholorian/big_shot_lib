package net.typho.big_shot_lib.impl.client.rendering.util

//? if >=1.21.6 {
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.impl.util.getExtensionValue
//? }

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.util.FogUtil
import net.typho.big_shot_lib.api.math.vec.NeoVec4f

object FogUtilImpl : FogUtil {
    override fun upload(shader: GlBoundProgram) {
        //? if <1.21.2 {
        /*shader.setUniform("FogStart") { set(RenderSystem.getShaderFogStart()) }
        shader.setUniform("FogEnd") { set(RenderSystem.getShaderFogEnd()) }
        shader.setUniform("FogShape") { set(RenderSystem.getShaderFogShape().index) }
        shader.setUniform("FogColor") {
            val color = RenderSystem.getShaderFogColor()
            setFloatVec(NeoVec4f(color[0], color[1], color[2], color[3]))
        }
        *///? } else if <1.21.6 {
        /*val fog = RenderSystem.getShaderFog()
        shader.setUniform("FogStart") { set(fog.start) }
        shader.setUniform("FogEnd") { set(fog.end) }
        shader.setUniform("FogShape") { set(fog.shape.index) }
        shader.setUniform("FogColor") { setFloatVec(NeoVec4f(fog.red, fog.green, fog.blue, fog.alpha)) }
        *///? } else {
        RenderSystem.getShaderFog()?.let { fog ->
            shader.setUniformBufferRange("Fog", fog.buffer.getExtensionValue<GlBuffer>(), fog.offset.toLong(), fog.length.toLong())
        }
        //? }
    }
}