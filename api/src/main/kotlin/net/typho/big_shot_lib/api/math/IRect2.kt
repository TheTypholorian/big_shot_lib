package net.typho.big_shot_lib.api.math

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Direction
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.typho.big_shot_lib.api.error.IllegalDimensionException
import kotlin.math.max
import kotlin.math.min

interface IRect2<N : Number> {
    val opSet: OperatorSet<N>

    val min: IVec2<N>
    val max: IVec2<N>

    val size: IVec2<N>
        get() = max - min
    val sizeInclusive: IVec2<N>
        get() = size + opSet.one
    val area: N
        get() {
            val size = size
            return opSet.times(size.x, size.y)
        }
    val areaInclusive: N
        get() {
            val size = sizeInclusive
            return opSet.times(size.x, size.y)
        }

    fun copyWith(min: IVec2<N>, max: IVec2<N>): IRect2<N>

    fun copyWithUnchecked(min: IVec2<N>, max: IVec2<N>): IRect2<N>

    fun include(other: IRect2<N>): IRect2<N> {
        return copyWithUnchecked(min.min(other.min), max.max(other.max))
    }

    fun include(other: IVec2<N>): IRect2<N> {
        return copyWithUnchecked(min.min(other), max.max(other))
    }

    fun contains(other: IRect2<N>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: IVec2<N>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: IRect2<N>): Boolean {
        return min.allLessThan(other.max) && max.allGreaterThan(other.min)
    }

    fun move(direction: Direction, amount: N): IRect2<N> {
        if (direction.axis == Direction.Axis.Z) {
            throw IllegalDimensionException(direction.toString())
        }

        return when (direction) {
            Direction.DOWN -> copyWithUnchecked(min.minus(opSet.zero, amount), max.minus(opSet.zero, amount))
            Direction.UP -> copyWithUnchecked(min.plus(opSet.zero, amount), max.plus(opSet.zero, amount))
            Direction.WEST -> copyWithUnchecked(min.minus(amount, opSet.zero), max.minus(amount, opSet.zero))
            Direction.EAST -> copyWithUnchecked(min.plus(amount, opSet.zero), max.plus(amount, opSet.zero))
            else -> throw IllegalDimensionException(direction.toString())
        }
    }

    fun move(amount: IVec2<N>): IRect2<N> {
        return copyWithUnchecked(min + amount, max + amount)
    }

    fun extend(direction: Direction, amount: N): IRect2<N> {
        return when (direction) {
            Direction.DOWN -> copyWith(min.minus(opSet.zero, amount), max)
            Direction.UP -> copyWith(min, max.plus(opSet.zero, amount))
            Direction.WEST -> copyWith(min.minus(amount, opSet.zero), max)
            Direction.EAST -> copyWith(min, max.plus(amount, opSet.zero))
            else -> throw IllegalDimensionException(direction.toString())
        }
    }

    fun expand(axis: Direction.Axis, amount: N): IRect2<N> {
        return when (axis) {
            Direction.Axis.X -> copyWith(min.minus(opSet.zero, amount), max.plus(opSet.zero, amount))
            Direction.Axis.Y -> copyWith(min.minus(amount, opSet.zero), max.plus(amount, opSet.zero))
            else -> throw IllegalDimensionException(axis.toString())
        }
    }

    operator fun iterator(): Iterator<IVec2<N>> {
        return iterator(opSet.one)
    }

    fun iterator(inc: N) = object : Iterator<IVec2<N>> {
        var x = min.x
        var y = min.y

        override fun hasNext(): Boolean {
            return opSet.lequalThan(x, max.x)
        }

        override fun next(): IVec2<N> {
            val pos = min.copyWith(x, y)

            y = opSet.plus(y, inc)

            if (opSet.greaterThan(y, max.y)) {
                y = min.y
                x = opSet.plus(x, inc)
            }

            return pos
        }
    }

    private class IntImpl(
        override val min: IVec2<Int>,
        override val max: IVec2<Int>
    ) : IRect2<Int> {
        override val opSet: OperatorSet<Int>
            get() = IntOperatorSet

        override fun copyWith(min: IVec2<Int>, max: IVec2<Int>) = IRect2(min, max)

        override fun copyWithUnchecked(min: IVec2<Int>, max: IVec2<Int>) = unchecked(min, max)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IRect2<*>) return false

            if (min != other.min) return false
            if (max != other.max) return false

            return true
        }

        override fun hashCode(): Int {
            var result = min.hashCode()
            result = 31 * result + max.hashCode()
            return result
        }

        override fun toString(): String {
            return "(min=$min, max=$max)"
        }
    }

    private class FloatImpl(
        override val min: IVec2<Float>,
        override val max: IVec2<Float>
    ) : IRect2<Float> {
        override val opSet: OperatorSet<Float>
            get() = FloatOperatorSet

        override fun copyWith(min: IVec2<Float>, max: IVec2<Float>) = IRect2(min, max)

        override fun copyWithUnchecked(min: IVec2<Float>, max: IVec2<Float>) = unchecked(min, max)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IRect2<*>) return false

            if (min != other.min) return false
            if (max != other.max) return false

            return true
        }

        override fun hashCode(): Int {
            var result = min.hashCode()
            result = 31 * result + max.hashCode()
            return result
        }

        override fun toString(): String {
            return "(min=$min, max=$max)"
        }
    }

    private class DoubleImpl(
        override val min: IVec2<Double>,
        override val max: IVec2<Double>
    ) : IRect2<Double> {
        override val opSet: OperatorSet<Double>
            get() = DoubleOperatorSet

        override fun copyWith(min: IVec2<Double>, max: IVec2<Double>) = IRect2(min, max)

        override fun copyWithUnchecked(min: IVec2<Double>, max: IVec2<Double>) = unchecked(min, max)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IRect2<*>) return false

            if (min != other.min) return false
            if (max != other.max) return false

            return true
        }

        override fun hashCode(): Int {
            var result = min.hashCode()
            result = 31 * result + max.hashCode()
            return result
        }

        override fun toString(): String {
            return "(min=$min, max=$max)"
        }
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<IRect2<Int>> = Codec.list(Codec.INT, 4, 4).xmap(
            { unchecked(it[0], it[1], it[2], it[3]) },
            { listOf(it.min.x, it.min.y, it.max.x, it.max.y) }
        )
        @JvmField
        val FLOAT_CODEC: Codec<IRect2<Float>> = Codec.list(Codec.FLOAT, 4, 4).xmap(
            { unchecked(it[0], it[1], it[2], it[3]) },
            { listOf(it.min.x, it.min.y, it.max.x, it.max.y) }
        )
        @JvmField
        val DOUBLE_CODEC: Codec<IRect2<Double>> = Codec.list(Codec.DOUBLE, 4, 4).xmap(
            { unchecked(it[0], it[1], it[2], it[3]) },
            { listOf(it.min.x, it.min.y, it.max.x, it.max.y) }
        )

        @JvmField
        val INT_STREAM_CODEC: StreamCodec<ByteBuf, IRect2<Int>> = StreamCodec.composite(
            ByteBufCodecs.INT, { it.min.x },
            ByteBufCodecs.INT, { it.min.y },
            ByteBufCodecs.INT, { it.max.x },
            ByteBufCodecs.INT, { it.max.y },
            ::unchecked
        )
        @JvmField
        val FLOAT_STREAM_CODEC: StreamCodec<ByteBuf, IRect2<Float>> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, { it.min.x },
            ByteBufCodecs.FLOAT, { it.min.y },
            ByteBufCodecs.FLOAT, { it.max.x },
            ByteBufCodecs.FLOAT, { it.max.y },
            ::unchecked
        )
        @JvmField
        val DOUBLE_STREAM_CODEC: StreamCodec<ByteBuf, IRect2<Double>> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, { it.min.x },
            ByteBufCodecs.DOUBLE, { it.min.y },
            ByteBufCodecs.DOUBLE, { it.max.x },
            ByteBufCodecs.DOUBLE, { it.max.y },
            ::unchecked
        )

        @JvmStatic
        @JvmName("of")
        operator fun invoke(minX: Int, minY: Int, maxX: Int, maxY: Int): IRect2<Int> = IntImpl(IVec2(min(minX, maxX), min(minY, maxY)), IVec2(max(minX, maxX), max(minY, maxY)))

        @JvmStatic
        @JvmName("ofInt")
        operator fun invoke(min: IVec2<Int>, max: IVec2<Int>): IRect2<Int> = invoke(min.x, min.y, max.x, max.y)

        @JvmStatic
        @JvmName("ofSize")
        fun size(x: Int, y: Int, w: Int, h: Int): IRect2<Int> = invoke(x, y, x + w, y + h)

        @JvmStatic
        @JvmName("ofSizeInt")
        fun size(pos: IVec2<Int>, size: IVec2<Int>): IRect2<Int> = size(pos.x, pos.y, size.x, size.y)

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(minX: Int, minY: Int, maxX: Int, maxY: Int): IRect2<Int> = IntImpl(IVec2(minX, minY), IVec2(maxX, maxY))

        @JvmStatic
        @JvmName("ofUncheckedInt")
        fun unchecked(min: IVec2<Int>, max: IVec2<Int>): IRect2<Int> = IntImpl(min, max)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(minX: Float, minY: Float, maxX: Float, maxY: Float): IRect2<Float> = FloatImpl(IVec2(min(minX, maxX), min(minY, maxY)), IVec2(max(minX, maxX), max(minY, maxY)))

        @JvmStatic
        @JvmName("ofFloat")
        operator fun invoke(min: IVec2<Float>, max: IVec2<Float>): IRect2<Float> = invoke(min.x, min.y, max.x, max.y)

        @JvmStatic
        @JvmName("ofSize")
        fun size(x: Float, y: Float, w: Float, h: Float): IRect2<Float> = invoke(x, y, x + w, y + h)

        @JvmStatic
        @JvmName("ofSizeFloat")
        fun size(pos: IVec2<Float>, size: IVec2<Float>): IRect2<Float> = size(pos.x, pos.y, size.x, size.y)

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(minX: Float, minY: Float, maxX: Float, maxY: Float): IRect2<Float> = FloatImpl(IVec2(minX, minY), IVec2(maxX, maxY))

        @JvmStatic
        @JvmName("ofUncheckedFloat")
        fun unchecked(min: IVec2<Float>, max: IVec2<Float>): IRect2<Float> = FloatImpl(min, max)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(minX: Double, minY: Double, maxX: Double, maxY: Double): IRect2<Double> = DoubleImpl(IVec2(min(minX, maxX), min(minY, maxY)), IVec2(max(minX, maxX), max(minY, maxY)))

        @JvmStatic
        @JvmName("ofDouble")
        operator fun invoke(min: IVec2<Double>, max: IVec2<Double>): IRect2<Double> = invoke(min.x, min.y, max.x, max.y)

        @JvmStatic
        @JvmName("ofSize")
        fun size(x: Double, y: Double, w: Double, h: Double): IRect2<Double> = invoke(x, y, x + w, y + h)

        @JvmStatic
        @JvmName("ofSizeDouble")
        fun size(pos: IVec2<Double>, size: IVec2<Double>): IRect2<Double> = size(pos.x, pos.y, size.x, size.y)

        @JvmStatic
        @JvmName("ofUnchecked")
        fun unchecked(minX: Double, minY: Double, maxX: Double, maxY: Double): IRect2<Double> = DoubleImpl(IVec2(minX, minY), IVec2(maxX, maxY))

        @JvmStatic
        @JvmName("ofUncheckedDouble")
        fun unchecked(min: IVec2<Double>, max: IVec2<Double>): IRect2<Double> = DoubleImpl(min, max)
    }
}