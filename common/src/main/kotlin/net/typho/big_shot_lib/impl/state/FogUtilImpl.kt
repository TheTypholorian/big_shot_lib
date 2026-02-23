package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.opengl.buffers.BufferType
import net.typho.big_shot_lib.api.client.opengl.shaders.GlShader
import net.typho.big_shot_lib.api.client.opengl.util.FogUtil
import net.typho.big_shot_lib.mixin.GlBufferAccessor

class FogUtilImpl : FogUtil {
    override fun upload(shader: GlShader) {
        val mojang = RenderSystem.getShaderFog() ?: return
        shader.getUniformBuffer("Fog")?.setRange(
            BufferType.UNIFORM_BUFFER,
            (mojang.buffer as GlBufferAccessor).handle,
            mojang.offset.toLong(),
            mojang.length.toLong()
        )
    }
}