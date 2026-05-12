package net.typho.big_shot_lib.impl.client.rendering.util

//? if >=1.21.6 {
/*import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.impl.util.getExtensionValue
*///? }

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.util.FogUtil

object FogUtilImpl : FogUtil {
    override fun upload(shader: GlBoundProgram) {
        //? if <1.21.5 {
        /*shader.setUniform("FogStart") { set(RenderSystem.getShaderFogStart()) }
        shader.setUniform("FogEnd") { set(RenderSystem.getShaderFogEnd()) }
        shader.setUniform("FogShape") { set(RenderSystem.getShaderFogShape().index) }
        *///? } else if <1.21.6 {
        val fog = RenderSystem.getShaderFog()
        shader.setUniform("FogStart") { set(fog.start) }
        shader.setUniform("FogEnd") { set(fog.end) }
        shader.setUniform("FogShape") { set(fog.shape.index) }
        //? } else {
        /*val fog = RenderSystem.getShaderFog()
        shader.setUniformBufferRange("Fog", fog.buffer.getExtensionValue<GlBuffer>(), fog.offset, fog.length)
        *///? }
    }
}