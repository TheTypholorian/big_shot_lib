package net.typho.big_shot_lib.api.client.rendering.common

import com.mojang.blaze3d.buffers.GpuBufferSlice
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.buffer.MemoryPointer
import java.nio.ByteBuffer

interface GpuBuffer : Extension<GpuBuffer>, GpuResource {
    override val type: GpuResourceType
        get() = GpuResourceType.BUFFER

    fun copyTo(buffer: GpuBuffer)

    fun upload(data: ByteBuffer)

    fun upload(data: MemoryPointer) {
        upload(data.asByteBuffer())
    }

    fun slice(): GpuBufferSlice

    fun slice(offset: Long, length: Long): GpuBufferSlice
}