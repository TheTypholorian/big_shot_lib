package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat

interface GlFramebufferAttachment {
    val format: GlTextureFormat?

    fun attach(target: Int, attachment: Int)
}