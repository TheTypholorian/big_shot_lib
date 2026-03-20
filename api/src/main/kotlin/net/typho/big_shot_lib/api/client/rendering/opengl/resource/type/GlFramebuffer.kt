package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.KeyedDelegate

interface GlFramebuffer : GlResource {
    val colorAttachments: KeyedDelegate.ReadOnly<Int, GlFramebufferAttachment?>
    val depthAttachment: GlFramebufferAttachment?

    fun bind(viewport: AbstractRect2<Int>? = null): GlBoundFramebuffer
}