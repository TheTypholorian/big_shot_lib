package net.typho.big_shot_lib.api.math

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Direction
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.typho.big_shot_lib.api.math.OperatorSet
import net.typho.big_shot_lib.api.math.IVec3
import kotlin.math.max
import kotlin.math.min

interface IRect3<N : Number> {
    val opSet: OperatorSet<N>

    val min: IVec3<N>
    val max: IVec3<N>

    val size: IVec3<N>
        get() = max - min
    val sizeInclusive: IVec3<N>
        get() = size + opSet.one
    val area: N
        get() {
            val size = size
            return opSet.times(size.x, opSet.times(size.y, size.z))
        }
    val areaInclusive: N
        get() {
            val size = sizeInclusive
            return opSet.times(size.x, opSet.times(size.y, size.z))
        }

    fun copyWith(min: IVec3<N>, max: IVec3<N>): IRect3<N>

    fun copyWithUnchecked(min: IVec3<N>, max: IVec3<N>): IRect3<N>

    fun include(other: IRect3<N>): IRect3<N> {
        return copyWithUnchecked(min.min(other.min), max.max(other.max))
    }

    fun include(other: IVec3<N>): IRect3<N> {
        return copyWithUnchecked(min.min(other), max.max(other))
    }

    fun contains(other: IRect3<N>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: IVec3<N>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: IRect3<N>): Boolean {
        return min.allLessThan(other.max) && max.allGreaterThan(other.min)
    }

    operator fun iterator(): Iterator<IVec3<N>> {
        return iterator(opSet.one)
    }

    fun iterator(inc: N) = object : Iterator<IVec3<N>> {
        var x = min.x
        var y = min.y
        var z = min.z

        override fun hasNext(): Boolean {
            return opSet.lequalThan(x, max.x)
        }

        override fun next(): IVec3<N> {
            val pos = min.copyWith(x, y, z)

            z = opSet.plus(z, inc)

            if (opSet.greaterThan(z, max.z)) {
                z = min.z
                y = opSet.plus(y, inc)

                if (opSet.greaterThan(y, max.y)) {
                    y = min.y
                    x = opSet.plus(x, inc)
                }
            }

            return pos
        }
    }

    fun move(direction: Direction, amount: N): IRect3<N> {
        return when (direction) {
            Direction.NORTH -> copyWithUnchecked(min.minus(opSet.zero, opSet.zero, amount), max.minus(opSet.zero, opSet.zero, amount))
            Direction.SOUTH -> copyWithUnchecked(min.plus(opSet.zero, opSet.zero, amount), max.plus(opSet.zero, opSet.zero, amount))
            Direction.DOWN -> copyWithUnchecked(min.minus(opSet.zero, amount, opSet.zero), max.minus(opSet.zero, amount, opSet.zero))
            Direction.UP -> copyWithUnchecked(min.plus(opSet.zero, amount, opSet.zero), max.plus(opSet.zero, amount, opSet.zero))
            Direction.WEST -> copyWithUnchecked(min.minus(amount, opSet.zero, opSet.zero), max.minus(amount, opSet.zero, opSet.zero))
            Direction.EAST -> copyWithUnchecked(min.plus(amount, opSet.zero, opSet.zero), max.plus(amount, opSet.zero, opSet.zero))
        }
    }

    fun move(amount: IVec3<N>): IRect3<N> {
        return copyWithUnchecked(min + amount, max + amount)
    }

    fun extend(direction: Direction, amount: N): IRect3<N> {
        return when (direction) {
            Direction.NORTH -> copyWith(min.minus(opSet.zero, opSet.zero, amount), max)
            Direction.SOUTH -> copyWith(min, max.plus(opSet.zero, opSet.zero, amount))
            Direction.DOWN -> copyWith(min.minus(opSet.zero, amount, opSet.zero), max)
            Direction.UP -> copyWith(min, max.plus(opSet.zero, amount, opSet.zero))
            Direction.WEST -> copyWith(min.minus(amount, opSet.zero, opSet.zero), max)
            Direction.EAST -> copyWith(min, max.plus(amount, opSet.zero, opSet.zero))
        }
    }

    fun expand(axis: Direction.Axis, amount: N): IRect3<N> {
        return when (axis) {
            Direction.Axis.X -> copyWith(min.minus(opSet.zero, amount, opSet.zero), max.plus(opSet.zero, amount, opSet.zero))
            Direction.Axis.Y -> copyWith(min.minus(amount, opSet.zero, opSet.zero), max.plus(amount, opSet.zero, opSet.zero))
            Direction.Axis.Z -> copyWith(min.minus(opSet.zero, opSet.zero, amount), max.plus(opSet.zero, opSet.zero, amount))
        }
    }

    private class IntImpl(
        override val min: IVec3<Int>,
        override val max: IVec3<Int>
    ) : IRect3<Int> {
        override val opSet: OperatorSet<Int>
            get() = IntOperatorSet

        override fun copyWith(min: IVec3<Int>, max: IVec3<Int>) = IRect3(min, max)

        override fun copyWithUnchecked(min: IVec3<Int>, max: IVec3<Int>) = unchecked(min, max)
    }

    private class FloatImpl(
        override val min: IVec3<Float>,
        override val max: IVec3<Float>
    ) : IRect3<Float> {
        override val opSet: OperatorSet<Float>
            get() = FloatOperatorSet

        override fun copyWith(min: IVec3<Float>, max: IVec3<Float>) = IRect3(min, max)

        override fun copyWithUnchecked(min: IVec3<Float>, max: IVec3<Float>) = unchecked(min, max)
    }

    private class DoubleImpl(
        override val min: IVec3<Double>,
        override val max: IVec3<Double>
    ) : IRect3<Double> {
        override val opSet: OperatorSet<Double>
            get() = DoubleOperatorSet

        override fun copyWith(min: IVec3<Double>, max: IVec3<Double>) = IRect3(min, max)

        override fun copyWithUnchecked(min: IVec3<Double>, max: IVec3<Double>) = unchecked(min, max)
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<IRect3<Int>> = Codec.list(Codec.INT, 6, 6).xmap(
            { unchecked(it[0], it[1], it[2], it[3], it[4], it[5]) },
            { listOf(it.min.x, it.min.y, it.min.z, it.max.x, it.max.y, it.max.z) }
        )
        @JvmField
        val FLOAT_CODEC: Codec<IRect3<Float>> = Codec.list(Codec.FLOAT, 6, 6).xmap(
            { unchecked(it[0], it[1], it[2], it[3], it[4], it[5]) },
            { listOf(it.min.x, it.min.y, it.min.z, it.max.x, it.max.y, it.max.z) }
        )
        @JvmField
        val DOUBLE_CODEC: Codec<IRect3<Double>> = Codec.list(Codec.DOUBLE, 6, 6).xmap(
            { unchecked(it[0], it[1], it[2], it[3], it[4], it[5]) },
            { listOf(it.min.x, it.min.y, it.min.z, it.max.x, it.max.y, it.max.z) }
        )

        @JvmField
        val INT_STREAM_CODEC: StreamCodec<ByteBuf, IRect3<Int>> = StreamCodec.composite(
            ByteBufCodecs.INT, { it.min.x },
            ByteBufCodecs.INT, { it.min.y },
            ByteBufCodecs.INT, { it.min.z },
            ByteBufCodecs.INT, { it.max.x },
            ByteBufCodecs.INT, { it.max.y },
            ByteBufCodecs.INT, { it.max.z },
            ::unchecked
        )
        @JvmField
        val FLOAT_STREAM_CODEC: StreamCodec<ByteBuf, IRect3<Float>> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, { it.min.x },
            ByteBufCodecs.FLOAT, { it.min.y },
            ByteBufCodecs.FLOAT, { it.min.z },
            ByteBufCodecs.FLOAT, { it.max.x },
            ByteBufCodecs.FLOAT, { it.max.y },
            ByteBufCodecs.FLOAT, { it.max.z },
            ::unchecked
        )
        @JvmField
        val DOUBLE_STREAM_CODEC: StreamCodec<ByteBuf, IRect3<Double>> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, { it.min.x },
            ByteBufCodecs.DOUBLE, { it.min.y },
            ByteBufCodecs.DOUBLE, { it.min.z },
            ByteBufCodecs.DOUBLE, { it.max.x },
            ByteBufCodecs.DOUBLE, { it.max.y },
            ByteBufCodecs.DOUBLE, { it.max.z },
            ::unchecked
        )

        @JvmStatic
        @JvmName("of")
        operator fun invoke(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int): IRect3<Int> = IntImpl(IVec3(min(minX, maxX), min(minY, maxY), min(minZ, maxZ)), IVec3(max(minX, maxX), max(minY, maxY), max(minZ, maxZ)))

        @JvmStatic
        @JvmName("of")
        operator fun invoke(min: IVec3<Int>, max: IVec3<Int>): IRect3<Int> = invoke(min.x, min.y, min.z, max.x, max.y, max.z)

        @JvmStatic
        @JvmName("ofSize")
        fun size(x: Int, y: Int, z: Int, w: Int, h: Int, d: Int): IRect3<Int> = invoke(x, y, z, x + w, y + h, z + d)

        @JvmStatic
        @JvmName("ofSize")
        fun size(pos: IVec3<Int>, size: IVec3<Int>): IRect3<Int> = size(pos.x, pos.y, pos.z, size.x, size.y, size.x)

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int): IRect3<Int> = IntImpl(IVec3(minX, minY, minZ), IVec3(maxX, maxY, maxZ))

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(min: IVec3<Int>, max: IVec3<Int>): IRect3<Int> = IntImpl(min, max)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): IRect3<Float> = FloatImpl(IVec3(min(minX, maxX), min(minY, maxY), min(minZ, maxZ)), IVec3(max(minX, maxX), max(minY, maxY), max(minZ, maxZ)))

        @JvmStatic
        @JvmName("of")
        operator fun invoke(min: IVec3<Float>, max: IVec3<Float>): IRect3<Float> = invoke(min.x, min.y, min.z, max.x, max.y, max.z)

        @JvmStatic
        @JvmName("ofSize")
        fun size(x: Float, y: Float, z: Float, w: Float, h: Float, d: Float): IRect3<Float> = invoke(x, y, z, x + w, y + h, z + d)

        @JvmStatic
        @JvmName("ofSize")
        fun size(pos: IVec3<Float>, size: IVec3<Float>): IRect3<Float> = size(pos.x, pos.y, pos.z, size.x, size.y, size.x)

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): IRect3<Float> = FloatImpl(IVec3(minX, minY, minZ), IVec3(maxX, maxY, maxZ))

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(min: IVec3<Float>, max: IVec3<Float>): IRect3<Float> = FloatImpl(min, max)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): IRect3<Double> = DoubleImpl(IVec3(min(minX, maxX), min(minY, maxY), min(minZ, maxZ)), IVec3(max(minX, maxX), max(minY, maxY), max(minZ, maxZ)))

        @JvmStatic
        @JvmName("of")
        operator fun invoke(min: IVec3<Double>, max: IVec3<Double>): IRect3<Double> = invoke(min.x, min.y, min.z, max.x, max.y, max.z)

        @JvmStatic
        @JvmName("ofSize")
        fun size(x: Double, y: Double, z: Double, w: Double, h: Double, d: Double): IRect3<Double> = invoke(x, y, z, x + w, y + h, z + d)

        @JvmStatic
        @JvmName("ofSize")
        fun size(pos: IVec3<Double>, size: IVec3<Double>): IRect3<Double> = size(pos.x, pos.y, pos.z, size.x, size.y, size.x)

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): IRect3<Double> = DoubleImpl(IVec3(minX, minY, minZ), IVec3(maxX, maxY, maxZ))

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(min: IVec3<Double>, max: IVec3<Double>): IRect3<Double> = DoubleImpl(min, max)
    }
}