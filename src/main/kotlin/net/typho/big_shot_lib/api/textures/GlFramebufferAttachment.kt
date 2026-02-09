package net.typho.big_shot_lib.api.textures

import net.typho.big_shot_lib.api.util.BufferUploader

interface GlFramebufferAttachment {
    fun getFormat(): TextureFormat

    fun attachToFramebuffer(attachment: Int)

    fun resize(width: Int, height: Int): BufferUploader?
}