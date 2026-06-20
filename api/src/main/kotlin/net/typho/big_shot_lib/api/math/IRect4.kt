package net.typho.big_shot_lib.api.math

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.typho.big_shot_lib.api.math.OperatorSet
import net.typho.big_shot_lib.api.math.IVec4
import kotlin.math.max
import kotlin.math.min

interface IRect4<N : Number> {
    val opSet: OperatorSet<N>

    val min: IVec4<N>
    val max: IVec4<N>

    val size: IVec4<N>
        get() = max - min
    val sizeInclusive: IVec4<N>
        get() = size + opSet.one
    val area: N
        get() {
            val size = size
            return opSet.times(size.x, opSet.times(size.y, opSet.times(size.z, size.w)))
        }
    val areaInclusive: N
        get() {
            val size = sizeInclusive
            return opSet.times(size.x, opSet.times(size.y, opSet.times(size.z, size.w)))
        }

    fun copyWith(min: IVec4<N>, max: IVec4<N>): IRect4<N>

    fun copyWithUnchecked(min: IVec4<N>, max: IVec4<N>): IRect4<N>

    fun include(other: IRect4<N>): IRect4<N> {
        return copyWith(min.min(other.min), max.max(other.max))
    }

    fun include(other: IVec4<N>): IRect4<N> {
        return copyWith(min.min(other), max.max(other))
    }

    fun contains(other: IRect4<N>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: IVec4<N>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: IRect4<N>): Boolean {
        return min.allLessThan(other.max) && max.allGreaterThan(other.min)
    }

    operator fun iterator(): Iterator<IVec4<N>> {
        return iterator(opSet.one)
    }

    fun iterator(inc: N) = object : Iterator<IVec4<N>> {
        var x = min.x
        var y = min.y
        var z = min.z
        var w = min.w

        override fun hasNext(): Boolean {
            return opSet.lequalThan(x, max.x)
        }

        override fun next(): IVec4<N> {
            val pos = min.copyWith(x, y, z, w)

            w = opSet.plus(w, inc)

            if (opSet.greaterThan(w, max.w)) {
                w = min.w
                z = opSet.plus(z, inc)

                if (opSet.greaterThan(z, max.z)) {
                    z = min.z
                    y = opSet.plus(y, inc)

                    if (opSet.greaterThan(y, max.y)) {
                        y = min.y
                        x = opSet.plus(x, inc)
                    }
                }
            }

            return pos
        }
    }

    fun move(amount: IVec4<N>): IRect4<N> {
        return copyWithUnchecked(min + amount, max + amount)
    }

    private class IntImpl(
        override val min: IVec4<Int>,
        override val max: IVec4<Int>
    ) : IRect4<Int> {
        override val opSet: OperatorSet<Int>
            get() = IntOperatorSet

        override fun copyWith(min: IVec4<Int>, max: IVec4<Int>) = IRect4(min, max)

        override fun copyWithUnchecked(min: IVec4<Int>, max: IVec4<Int>) = unchecked(min, max)
    }

    private class FloatImpl(
        override val min: IVec4<Float>,
        override val max: IVec4<Float>
    ) : IRect4<Float> {
        override val opSet: OperatorSet<Float>
            get() = FloatOperatorSet

        override fun copyWith(min: IVec4<Float>, max: IVec4<Float>) = IRect4(min, max)

        override fun copyWithUnchecked(min: IVec4<Float>, max: IVec4<Float>) = unchecked(min, max)
    }

    private class DoubleImpl(
        override val min: IVec4<Double>,
        override val max: IVec4<Double>
    ) : IRect4<Double> {
        override val opSet: OperatorSet<Double>
            get() = DoubleOperatorSet

        override fun copyWith(min: IVec4<Double>, max: IVec4<Double>) = IRect4(min, max)

        override fun copyWithUnchecked(min: IVec4<Double>, max: IVec4<Double>) = unchecked(min, max)
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<IRect4<Int>> = Codec.list(Codec.INT, 8, 8).xmap(
            { unchecked(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7]) },
            { listOf(it.min.x, it.min.y, it.min.z, it.min.w, it.max.x, it.max.y, it.max.z, it.max.w) }
        )
        @JvmField
        val FLOAT_CODEC: Codec<IRect4<Float>> = Codec.list(Codec.FLOAT, 8, 8).xmap(
            { unchecked(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7]) },
            { listOf(it.min.x, it.min.y, it.min.z, it.min.w, it.max.x, it.max.y, it.max.z, it.max.w) }
        )
        @JvmField
        val DOUBLE_CODEC: Codec<IRect4<Double>> = Codec.list(Codec.DOUBLE, 8, 8).xmap(
            { unchecked(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7]) },
            { listOf(it.min.x, it.min.y, it.min.z, it.min.w, it.max.x, it.max.y, it.max.z, it.max.w) }
        )

        @JvmField
        val INT_STREAM_CODEC: StreamCodec<ByteBuf, IRect4<Int>> = StreamCodec.composite(
            ByteBufCodecs.INT, { it.min.x },
            ByteBufCodecs.INT, { it.min.y },
            ByteBufCodecs.INT, { it.min.z },
            ByteBufCodecs.INT, { it.min.w },
            ByteBufCodecs.INT, { it.max.x },
            ByteBufCodecs.INT, { it.max.y },
            ByteBufCodecs.INT, { it.max.z },
            ByteBufCodecs.INT, { it.max.w },
            ::unchecked
        )
        @JvmField
        val FLOAT_STREAM_CODEC: StreamCodec<ByteBuf, IRect4<Float>> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, { it.min.x },
            ByteBufCodecs.FLOAT, { it.min.y },
            ByteBufCodecs.FLOAT, { it.min.z },
            ByteBufCodecs.FLOAT, { it.min.w },
            ByteBufCodecs.FLOAT, { it.max.x },
            ByteBufCodecs.FLOAT, { it.max.y },
            ByteBufCodecs.FLOAT, { it.max.z },
            ByteBufCodecs.FLOAT, { it.max.w },
            ::unchecked
        )
        @JvmField
        val DOUBLE_STREAM_CODEC: StreamCodec<ByteBuf, IRect4<Double>> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, { it.min.x },
            ByteBufCodecs.DOUBLE, { it.min.y },
            ByteBufCodecs.DOUBLE, { it.min.z },
            ByteBufCodecs.DOUBLE, { it.min.w },
            ByteBufCodecs.DOUBLE, { it.max.x },
            ByteBufCodecs.DOUBLE, { it.max.y },
            ByteBufCodecs.DOUBLE, { it.max.z },
            ByteBufCodecs.DOUBLE, { it.max.w },
            ::unchecked
        )

        @JvmStatic
        @JvmName("of")
        operator fun invoke(minX: Int, minY: Int, minZ: Int, minW: Int, maxX: Int, maxY: Int, maxZ: Int, maxW: Int): IRect4<Int> = IntImpl(IVec4(min(minX, maxX), min(minY, maxY), min(minZ, maxZ), min(minW, maxW)), IVec4(max(minX, maxX), max(minY, maxY), max(minZ, maxZ), max(minW, maxW)))

        @JvmStatic
        @JvmName("of")
        operator fun invoke(min: IVec4<Int>, max: IVec4<Int>): IRect4<Int> = invoke(min.x, min.y, min.z, min.w, max.x, max.y, max.z, max.w)

        @JvmStatic
        @JvmName("ofSize")
        fun size(x: Int, y: Int, z: Int, w: Int, width: Int, height: Int, depth: Int, time: Int): IRect4<Int> = invoke(x, y, z, w, x + width, y + height, z + depth, w + time)

        @JvmStatic
        @JvmName("ofSize")
        fun size(pos: IVec4<Int>, size: IVec4<Int>): IRect4<Int> = size(pos.x, pos.y, pos.z, pos.w, size.x, size.y, size.x, size.w)

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(minX: Int, minY: Int, minZ: Int, minW: Int, maxX: Int, maxY: Int, maxZ: Int, maxW: Int): IRect4<Int> = IntImpl(IVec4(minX, minY, minZ, minW), IVec4(maxX, maxY, maxZ, maxW))

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(min: IVec4<Int>, max: IVec4<Int>): IRect4<Int> = IntImpl(min, max)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(minX: Float, minY: Float, minZ: Float, minW: Float, maxX: Float, maxY: Float, maxZ: Float, maxW: Float): IRect4<Float> = FloatImpl(IVec4(min(minX, maxX), min(minY, maxY), min(minZ, maxZ), min(minW, maxW)), IVec4(max(minX, maxX), max(minY, maxY), max(minZ, maxZ), max(minW, maxW)))

        @JvmStatic
        @JvmName("of")
        operator fun invoke(min: IVec4<Float>, max: IVec4<Float>): IRect4<Float> = invoke(min.x, min.y, min.z, min.w, max.x, max.y, max.z, max.w)

        @JvmStatic
        @JvmName("ofSize")
        fun size(x: Float, y: Float, z: Float, w: Float, width: Float, height: Float, depth: Float, time: Float): IRect4<Float> = invoke(x, y, z, w, x + width, y + height, z + depth, w + time)

        @JvmStatic
        @JvmName("ofSize")
        fun size(pos: IVec4<Float>, size: IVec4<Float>): IRect4<Float> = size(pos.x, pos.y, pos.z, pos.w, size.x, size.y, size.x, size.w)

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(minX: Float, minY: Float, minZ: Float, minW: Float, maxX: Float, maxY: Float, maxZ: Float, maxW: Float): IRect4<Float> = FloatImpl(IVec4(minX, minY, minZ, minW), IVec4(maxX, maxY, maxZ, maxW))

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(min: IVec4<Float>, max: IVec4<Float>): IRect4<Float> = FloatImpl(min, max)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(minX: Double, minY: Double, minZ: Double, minW: Double, maxX: Double, maxY: Double, maxZ: Double, maxW: Double): IRect4<Double> = DoubleImpl(IVec4(min(minX, maxX), min(minY, maxY), min(minZ, maxZ), min(minW, maxW)), IVec4(max(minX, maxX), max(minY, maxY), max(minZ, maxZ), max(minW, maxW)))

        @JvmStatic
        @JvmName("of")
        operator fun invoke(min: IVec4<Double>, max: IVec4<Double>): IRect4<Double> = invoke(min.x, min.y, min.z, min.w, max.x, max.y, max.z, max.w)

        @JvmStatic
        @JvmName("ofSize")
        fun size(x: Double, y: Double, z: Double, w: Double, width: Double, height: Double, depth: Double, time: Double): IRect4<Double> = invoke(x, y, z, w, x + width, y + height, z + depth, w + time)

        @JvmStatic
        @JvmName("ofSize")
        fun size(pos: IVec4<Double>, size: IVec4<Double>): IRect4<Double> = size(pos.x, pos.y, pos.z, pos.w, size.x, size.y, size.x, size.w)

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(minX: Double, minY: Double, minZ: Double, minW: Double, maxX: Double, maxY: Double, maxZ: Double, maxW: Double): IRect4<Double> = DoubleImpl(IVec4(minX, minY, minZ, minW), IVec4(maxX, maxY, maxZ, maxW))

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(min: IVec4<Double>, max: IVec4<Double>): IRect4<Double> = DoubleImpl(min, max)
    }
}