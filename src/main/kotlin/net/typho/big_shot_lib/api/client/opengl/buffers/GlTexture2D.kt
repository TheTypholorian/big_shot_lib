package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.state.GlTextureType
import net.typho.big_shot_lib.api.client.opengl.util.WrappingType

interface GlTexture2D : GlTexture, GlFramebufferAttachment {
    override val type: GlTextureType
        get() = GlTextureType.TEXTURE_2D

    override fun bind(tracker: GlStateTracker): Bound<*>

    interface Bound<T : GlTexture2D> : GlTexture.Bound<T>, GlFramebufferAttachment.Bound<T> {
        var sWrapping: WrappingType
        var tWrapping: WrappingType
    }
}