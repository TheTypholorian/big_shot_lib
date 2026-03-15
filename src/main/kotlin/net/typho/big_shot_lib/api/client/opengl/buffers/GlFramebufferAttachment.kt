package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat

interface GlFramebufferAttachment {
    val format: TextureFormat

    fun attachToFramebuffer(attachment: Int, width: Int, height: Int, tracker: GlStateTracker = OpenGL.INSTANCE)
}