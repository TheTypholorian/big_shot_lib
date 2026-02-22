package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.shaders.GlShader
import net.typho.big_shot_lib.api.client.rendering.state.FogUtil
import net.typho.big_shot_lib.api.util.IColor

class FogUtilImpl : FogUtil {
    override fun upload(shader: GlShader) {
        val fog = RenderSystem.getShaderFog()
        shader.getUniform("FogStart")?.setValue(fog.start)
        shader.getUniform("FogEnd")?.setValue(fog.end)
        shader.getUniform("FogShape")?.setValue(fog.shape.index)
        shader.getUniform("FogColor")?.setValue(IColor.RGBAF(fog.red, fog.green, fog.blue, fog.alpha))
    }
}