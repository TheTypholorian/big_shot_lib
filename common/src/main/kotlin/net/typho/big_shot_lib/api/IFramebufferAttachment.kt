package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.TextureFormat

interface IFramebufferAttachment {
    fun format(): TextureFormat

    fun attach(attachment: Int, target: Int)

    fun resize2D(width: Int, height: Int)
}