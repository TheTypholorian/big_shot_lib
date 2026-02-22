package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.shaders.GlShader
import net.typho.big_shot_lib.api.client.rendering.state.FogUtil
import net.typho.big_shot_lib.mixin.GlBufferAccessor
import org.lwjgl.opengl.GL30.glBindBufferRange
import org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER

class FogUtilImpl : FogUtil {
    override fun upload(shader: GlShader) {
        val mojang = RenderSystem.getShaderFog() ?: return
        glBindBufferRange(
            GL_UNIFORM_BUFFER,
            0, // TODO
            //glGetUniformBlockIndex((shader as GlNamed).glId(), "Fog"),
            (mojang.buffer as GlBufferAccessor).handle,
            mojang.offset.toLong(),
            mojang.length.toLong()
        )
    }
}