package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.state.GlTextureType
import net.typho.big_shot_lib.api.client.opengl.util.WrappingType
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlTexture3D : GlTexture<GlTexture3D.Bound> {
    override val type: GlTextureType
        get() = GlTextureType.TEXTURE_3D

    override fun bind(tracker: GlStateTracker): Bound

    interface Bound : GlTexture.Bound {
        var sWrapping: WrappingType
        var tWrapping: WrappingType
        var rWrapping: WrappingType

        fun resize(width: Int, height: Int, depth: Int, upload: (uploader: BufferUploader) -> Unit = { it.uploadNull() })
    }
}