package net.typho.big_shot_lib.framebuffers

import net.typho.big_shot_lib.gl.resource.TextureFormat

interface IFramebufferAttachment {
    fun format(): TextureFormat

    fun attach2D(attachment: Int, target: Int)

    fun resize2D(width: Int, height: Int)
}