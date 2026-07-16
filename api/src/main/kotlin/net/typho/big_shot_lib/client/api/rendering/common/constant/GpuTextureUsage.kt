package net.typho.big_shot_lib.client.api.rendering.common.constant

@JvmInline
value class GpuTextureUsage(val flags: Int) {
    infix fun or(other: GpuTextureUsage) = GpuTextureUsage(flags or other.flags)

    companion object {
        @JvmStatic
        @get:JvmName("copyDst")
        val COPY_DST = GpuTextureUsage(1)
        @JvmStatic
        @get:JvmName("copySrc")
        val COPY_SRC = GpuTextureUsage(2)
        @JvmStatic
        @get:JvmName("textureBinding")
        val TEXTURE_BINDING = GpuTextureUsage(4)
        @JvmStatic
        @get:JvmName("renderAttachment")
        val RENDER_ATTACHMENT = GpuTextureUsage(8)
        @JvmStatic
        @get:JvmName("cubemapCompatible")
        val CUBEMAP_COMPATIBLE = GpuTextureUsage(16)
    }
}