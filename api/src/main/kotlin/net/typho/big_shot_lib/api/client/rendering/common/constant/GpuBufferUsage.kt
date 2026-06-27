package net.typho.big_shot_lib.api.client.rendering.common.constant

@JvmInline
value class GpuBufferUsage(val flags: Int) {
    infix fun or(other: GpuBufferUsage) = GpuBufferUsage(flags or other.flags)

    companion object {
        @JvmStatic
        @get:JvmName("mapRead")
        val MAP_READ = GpuBufferUsage(1)
        @JvmStatic
        @get:JvmName("mapWrite")
        val MAP_WRITE = GpuBufferUsage(2)
        @JvmStatic
        @get:JvmName("hintClientStorage")
        val HINT_CLIENT_STORAGE = GpuBufferUsage(4)
        @JvmStatic
        @get:JvmName("copyDst")
        val COPY_DST = GpuBufferUsage(8)
        @JvmStatic
        @get:JvmName("copySrc")
        val COPY_SRC = GpuBufferUsage(16)
        @JvmStatic
        @get:JvmName("vertex")
        val VERTEX = GpuBufferUsage(32)
        @JvmStatic
        @get:JvmName("index")
        val INDEX = GpuBufferUsage(64)
        @JvmStatic
        @get:JvmName("uniform")
        val UNIFORM = GpuBufferUsage(128)
        @JvmStatic
        @get:JvmName("uniformTexelBuffer")
        val UNIFORM_TEXEL_BUFFER = GpuBufferUsage(256)
        @JvmStatic
        @get:JvmName("indirectParameters")
        val INDIRECT_PARAMETERS = GpuBufferUsage(512)
    }
}