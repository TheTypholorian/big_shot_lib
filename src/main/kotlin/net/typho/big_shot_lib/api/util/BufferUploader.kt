package net.typho.big_shot_lib.api.util

import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.*

interface BufferUploader {
    fun upload(buffer: ByteBuffer)

    fun upload(buffer: CharBuffer) = upload(MemoryUtil.memByteBuffer(buffer))

    fun upload(buffer: DoubleBuffer) = upload(MemoryUtil.memByteBuffer(buffer))

    fun upload(buffer: FloatBuffer) = upload(MemoryUtil.memByteBuffer(buffer))

    fun upload(buffer: IntBuffer) = upload(MemoryUtil.memByteBuffer(buffer))

    fun upload(buffer: LongBuffer) = upload(MemoryUtil.memByteBuffer(buffer))

    fun upload(buffer: ShortBuffer) = upload(MemoryUtil.memByteBuffer(buffer))

    fun upload(address: Long, size: Int) = upload(MemoryUtil.memByteBuffer(address, size))

    fun uploadNull() = upload(NULL, 0)
}