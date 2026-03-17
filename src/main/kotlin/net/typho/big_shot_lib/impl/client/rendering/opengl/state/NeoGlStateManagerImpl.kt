package net.typho.big_shot_lib.impl.client.rendering.opengl.state

//? if <1.21.5 {
import com.mojang.blaze3d.platform.GlStateManager
//? } else {
/*import com.mojang.blaze3d.opengl.GlStateManager
*///? }

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.util.EnumArrayMap
import net.typho.big_shot_lib.api.util.enumArrayMapOf
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL41.GL_PROGRAM_PIPELINE_BINDING
import org.lwjgl.opengl.GL41.glBindProgramPipeline

object NeoGlStateManagerImpl : NeoGlStateManager {
    override val buffers: EnumArrayMap<GlBufferTarget, GlStateStack<Int>> = enumArrayMapOf { target ->
        GlStateStack.Impl(
            { GlStateManager._glBindBuffer(target.glId, it) },
            { glGetInteger(target.bindingId) }
        )
    }
    override val program: GlStateStack<Int> = GlStateStack.Impl(
        { GlStateManager._glUseProgram(it) },
        { glGetInteger(GL_CURRENT_PROGRAM) }
    )
    override val programPipeline: GlStateStack<Int> = GlStateStack.Impl(
        { glBindProgramPipeline(it) },
        { glGetInteger(GL_PROGRAM_PIPELINE_BINDING) }
    )
    override val vertexArray: GlStateStack<Int> = GlStateStack.Impl(
        { GlStateManager._glBindVertexArray(it) },
        { glGetInteger(GL_VERTEX_ARRAY_BINDING) }
    )
    override val textures: EnumArrayMap<GlTextureTarget, GlStateStack<Int>> = enumArrayMapOf { target ->
        if (target == GlTextureTarget.TEXTURE_2D)
            GlStateStack.Impl(
                { GlStateManager._bindTexture(it) },
                { GlStateManager.TEXTURES[activeTexture].binding }
            )
        else
            GlStateStack.Impl(
                { glBindTexture(target.glId, it) },
                { glGetInteger(target.bindingId) }
            )
    }
    override val renderbuffer: GlStateStack<Int> = GlStateStack.Impl(
        //? if <1.21.5 {
        { GlStateManager._glBindRenderbuffer(GL_RENDERBUFFER, it) },
        //? } else {
        /*{ glBindRenderbuffer(GL_RENDERBUFFER, it) },
        *///? }
        { glGetInteger(GL_RENDERBUFFER_BINDING) }
    )
    override val framebuffer: GlStateStack<Int> = GlStateStack.Impl(
        { GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, it) },
        //? if <1.21.5 {
        { GlStateManager.getBoundFramebuffer() }
        //? } else {
        /*{ glGetInteger(GL_FRAMEBUFFER_BINDING) }
        *///? }
    )
    override var activeTexture: Int
        //? if <1.21.5 {
        get() = GlStateManager._getActiveTexture()
        //? } else {
        /*get() = GlStateManager.activeTexture
        *///? }
        set(value) = GlStateManager._activeTexture(value)
}