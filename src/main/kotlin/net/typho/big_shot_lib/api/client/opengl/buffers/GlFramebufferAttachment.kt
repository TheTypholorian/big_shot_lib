package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlFramebufferAttachment {
    val format: TextureFormat

    fun attachToFramebuffer(attachment: Int)

    fun resize(width: Int, height: Int, upload: (uploader: BufferUploader) -> Unit = { it.uploadNull() })
}