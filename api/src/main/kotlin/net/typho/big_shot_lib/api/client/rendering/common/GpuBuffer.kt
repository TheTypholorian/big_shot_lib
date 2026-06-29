package net.typho.big_shot_lib.api.client.rendering.common

import com.mojang.blaze3d.buffers.GpuBufferSlice
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.buffer.MemoryPointer
import net.typho.big_shot_lib.api.util.buffer.MemoryWriter
import java.nio.ByteBuffer
import java.util.function.Consumer

interface GpuBuffer : Extension<GpuBuffer>, GpuResource {
    val size: Long
    override val type: GpuResourceType
        get() = GpuResourceType.BUFFER

    fun copyTo(buffer: GpuBuffer)

    fun upload(data: ByteBuffer)

    fun upload(data: MemoryPointer) {
        upload(data.asByteBuffer())
    }

    fun upload(data: Consumer<MemoryWriter>) {
        MemoryPointer.alloc(size).use {
            data.accept(it.write())
            upload(it)
        }
    }

    fun slice(): GpuBufferSlice

    fun slice(offset: Long, length: Long): GpuBufferSlice
}