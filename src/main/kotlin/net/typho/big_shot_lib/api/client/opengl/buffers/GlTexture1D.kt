package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.state.GlTextureType
import net.typho.big_shot_lib.api.client.opengl.util.WrappingType
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlTexture1D : GlTexture {
    override val type: GlTextureType
        get() = GlTextureType.TEXTURE_1D

    override fun bind(tracker: GlStateTracker): Bound<*>

    interface Bound<T : GlTexture1D> : GlTexture.Bound<T> {
        var sWrapping: WrappingType

        fun resize(width: Int, upload: (uploader: BufferUploader) -> Unit = { it.uploadNull() })
    }
}