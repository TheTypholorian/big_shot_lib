package net.typho.big_shot_lib.buffers

import net.typho.big_shot_lib.gl.resource.GlResourceInstance
import java.nio.ByteBuffer

interface IBuffer : GlResourceInstance {
    fun usage(): BufferUsage

    fun upload(buffer: ByteBuffer)
}