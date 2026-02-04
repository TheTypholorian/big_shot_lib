package net.typho.big_shot_lib.api.util

import com.mojang.serialization.Codec
import org.joml.*
import java.util.function.Function

object VectorCodecs {
    @JvmField
    val VEC2I: Codec<Vector2ic> = of(2, Codec.INT, { Vector2i(it[0], it[1]) }, { listOf(it.x(), it.y()) })
    @JvmField
    val VEC2F: Codec<Vector2fc> = of(2, Codec.FLOAT, { Vector2f(it[0], it[1]) }, { listOf(it.x(), it.y()) })
    @JvmField
    val VEC2D: Codec<Vector2dc> = of(2, Codec.DOUBLE, { Vector2d(it[0], it[1]) }, { listOf(it.x(), it.y()) })

    @JvmField
    val VEC3I: Codec<Vector3ic> = of(3, Codec.INT, { Vector3i(it[0], it[1], it[2]) }, { listOf(it.x(), it.y(), it.z()) })
    @JvmField
    val VEC3F: Codec<Vector3fc> = of(3, Codec.FLOAT, { Vector3f(it[0], it[1], it[2]) }, { listOf(it.x(), it.y(), it.z()) })
    @JvmField
    val VEC3D: Codec<Vector3dc> = of(3, Codec.DOUBLE, { Vector3d(it[0], it[1], it[2]) }, { listOf(it.x(), it.y(), it.z()) })

    @JvmField
    val VEC4I: Codec<Vector4ic> = of(4, Codec.INT, { Vector4i(it[0], it[1], it[2], it[3]) }, { listOf(it.x(), it.y(), it.z(), it.w()) })
    @JvmField
    val VEC4F: Codec<Vector4fc> = of(4, Codec.FLOAT, { Vector4f(it[0], it[1], it[2], it[3]) }, { listOf(it.x(), it.y(), it.z(), it.w()) })
    @JvmField
    val VEC4D: Codec<Vector4dc> = of(4, Codec.DOUBLE, { Vector4d(it[0], it[1], it[2], it[3]) }, { listOf(it.x(), it.y(), it.z(), it.w()) })

    fun <V, T> of(size: Int, codec: Codec<T>, to: Function<List<T>, V>, from: Function<V, List<T>>): Codec<V> {
        return codec.listOf(size, size).xmap(to, from)
    }
}