package net.typho.big_shot_lib.framebuffers

import net.typho.big_shot_lib.textures.TextureFormat

interface IFramebufferAttachment {
    fun getFormat(): TextureFormat

    fun attachToFramebuffer(attachment: Int, target: Int)

    fun resize(width: Int, height: Int)
}