package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

interface GlFramebufferAttachment {
    fun attach(target: Int, attachment: Int, width: Int, height: Int)
}