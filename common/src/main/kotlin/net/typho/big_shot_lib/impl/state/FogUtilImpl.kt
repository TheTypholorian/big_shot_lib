package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.opengl.shaders.GlShader
import net.typho.big_shot_lib.api.client.opengl.util.FogUtil
import net.typho.big_shot_lib.api.util.IColor

class FogUtilImpl : FogUtil {
    override fun upload(shader: GlShader) {
        shader.getUniform("FogStart")?.setValue(RenderSystem.getShaderFogStart())
        shader.getUniform("FogEnd")?.setValue(RenderSystem.getShaderFogEnd())
        shader.getUniform("FogShape")?.setValue(RenderSystem.getShaderFogShape().ordinal)
        shader.getUniform("FogColor")?.setValue(IColor.RGBAF(RenderSystem.getShaderFogColor()))
    }
}