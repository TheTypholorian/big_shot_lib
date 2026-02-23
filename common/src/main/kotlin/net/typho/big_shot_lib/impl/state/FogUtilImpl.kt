package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.opengl.shaders.GlShader
import net.typho.big_shot_lib.api.client.opengl.util.FogUtil
import net.typho.big_shot_lib.api.util.IColor

class FogUtilImpl : FogUtil {
    override fun upload(shader: GlShader) {
        val mojang = RenderSystem.getShaderFog()
        shader.getUniform("FogStart")?.setValue(mojang.start)
        shader.getUniform("FogEnd")?.setValue(mojang.end)
        shader.getUniform("FogShape")?.setValue(mojang.shape.index)
        shader.getUniform("FogColor")?.setValue(IColor.RGBAF(mojang.red, mojang.green, mojang.blue, mojang.alpha))
    }
}