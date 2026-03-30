package net.typho.big_shot_lib.impl.client.rendering.internal

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebufferAttachment
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.KeyedDelegate

internal class BoundMinecraftRenderTarget(
    resource: GlFramebuffer,
    viewport: AbstractRect2<Int>?,
    handle: GlStateStack.Handle<Int>
) : GlBoundFramebuffer.Basic(resource, viewport, handle) {
    override val colorAttachments: KeyedDelegate<Int, GlFramebufferAttachment?> = resource.colorAttachments.withSet { key, attachment ->
        throw UnsupportedOperationException("Cannot modify a Minecraft RenderTarget's attachments")
    }
    override var depthAttachment: GlFramebufferAttachment?
        get() = resource.depthAttachment
        set(value) {
            throw UnsupportedOperationException("Cannot modify a Minecraft RenderTarget's attachments")
        }
}