package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlFramebufferAttachment {
    fun getFormat(): TextureFormat

    fun attachToFramebuffer(attachment: Int)

    fun resize(width: Int, height: Int): BufferUploader?
}