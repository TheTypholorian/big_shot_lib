package net.typho.big_shot_lib.api.client.rendering.util

object PackedLight {
    const val FULL_BRIGHT = 0xF000F0
    const val FULL_SKY = 0xF00000
    const val FULL_BLOCK = 0xF0

    @JvmStatic
    fun pack(sky: Int, block: Int) = (sky shl 20) or (block shl 4)

    @JvmStatic
    fun unpackSky(packed: Int) = (packed ushr 20) and 0xF

    @JvmStatic
    fun unpackBlock(packed: Int) = (packed ushr 4) and 0xF
}