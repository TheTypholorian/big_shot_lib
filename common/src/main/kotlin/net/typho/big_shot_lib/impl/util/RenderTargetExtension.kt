package net.typho.big_shot_lib.impl.util

import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebufferAttachment

interface RenderTargetExtension {
    fun `big_shot_lib$getColorAttachments`() : List<GlFramebufferAttachment>

    fun `big_shot_lib$setColorAttachments`(attachments: List<GlFramebufferAttachment>)

    fun `big_shot_lib$getDepthAttachment`(): GlFramebufferAttachment?

    fun `big_shot_lib$setDepthAttachment`(attachment: GlFramebufferAttachment?)
}