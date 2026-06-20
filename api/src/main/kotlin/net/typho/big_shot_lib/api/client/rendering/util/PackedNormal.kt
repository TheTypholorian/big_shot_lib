package net.typho.big_shot_lib.api.client.rendering.util

object PackedNormal {
    @JvmStatic
    fun pack(x: Float, y: Float, z: Float): Int {
        return pack((x * 127).toInt(), (y * 127).toInt(), (z * 127).toInt())
    }

    @JvmStatic
    fun pack(x: Int, y: Int, z: Int): Int {
        return (x shl 16) or (y shl 8) or z
    }

    @JvmStatic
    fun unpackByteX(packed: Int): Byte {
        return (packed ushr 16).toByte()
    }

    @JvmStatic
    fun unpackByteY(packed: Int): Byte {
        return ((packed ushr 8) and 0xFF).toByte()
    }

    @JvmStatic
    fun unpackByteZ(packed: Int): Byte {
        return (packed and 0xFF).toByte()
    }

    @JvmStatic
    fun unpackX(packed: Int): Float {
        return unpackByteX(packed).toFloat() / 127
    }

    @JvmStatic
    fun unpackY(packed: Int): Float {
        return unpackByteY(packed).toFloat() / 127
    }

    @JvmStatic
    fun unpackZ(packed: Int): Float {
        return unpackByteZ(packed).toFloat() / 127
    }
}