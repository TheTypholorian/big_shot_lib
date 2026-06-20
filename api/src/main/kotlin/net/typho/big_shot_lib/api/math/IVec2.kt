package net.typho.big_shot_lib.api.math.vec

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Direction
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.typho.big_shot_lib.api.error.IllegalDimensionException
import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3dc
import org.joml.Vector3fc
import org.joml.Vector3ic

interface IVec2<N : Number> {
    val opSet: OperatorSet<N>

    val x: N
    val y: N

    val gridLength: N
        get() = opSet.max(opSet.abs(x), opSet.abs(y))
    val lengthSquared: N
        get() = opSet.plus(opSet.times(x, x), opSet.times(y, y))
    val length: Float
        get() = opSet.sqrt(lengthSquared)
    val abs: IVec2<N>
        get() = copyWith(opSet.abs(x), opSet.abs(y))

    fun copyWith(x: N, y: N): IVec2<N>

    fun toInt(): IVec2<Int> = IVec2(x.toInt(), y.toInt())

    fun toFloat(): IVec2<Float> = IVec2(x.toFloat(), y.toFloat())

    fun toDouble(): IVec2<Double> = IVec2(x.toDouble(), y.toDouble())

    fun lerp(x: N, y: N, d: Float): IVec2<N> {
        return copyWith(opSet.lerp(this.x, x, d), opSet.lerp(this.y, y, d))
    }

    fun plus(x: N, y: N): IVec2<N> {
        return copyWith(opSet.plus(this.x, x), opSet.plus(this.y, y))
    }

    fun minus(x: N, y: N): IVec2<N> {
        return copyWith(opSet.minus(this.x, x), opSet.minus(this.y, y))
    }

    fun times(x: N, y: N): IVec2<N> {
        return copyWith(opSet.times(this.x, x), opSet.times(this.y, y))
    }

    fun div(x: N, y: N): IVec2<N> {
        return copyWith(opSet.div(this.x, x), opSet.div(this.y, y))
    }

    fun rem(x: N, y: N): IVec2<N> {
        return copyWith(opSet.rem(this.x, x), opSet.rem(this.y, y))
    }

    fun min(x: N, y: N): IVec2<N> {
        return copyWith(opSet.min(this.x, x), opSet.min(this.y, y))
    }

    fun max(x: N, y: N): IVec2<N> {
        return copyWith(opSet.max(this.x, x), opSet.max(this.y, y))
    }

    fun distance(x: N, y: N): Float {
        return minus(x, y).length
    }

    fun distanceSquared(x: N, y: N): N {
        return minus(x, y).lengthSquared
    }

    fun gridDistance(x: N, y: N): N {
        return minus(x, y).gridLength
    }

    fun inDistance(x: N, y: N, dist: N): Boolean {
        return inDistanceSquared(x, y, opSet.times(dist, dist))
    }

    fun inDistanceSquared(x: N, y: N, dist: N): Boolean {
        return opSet.lessThan(distanceSquared(x, y), dist)
    }

    fun inGridDistance(x: N, y: N, dist: N): Boolean {
        return opSet.lessThan(gridDistance(x, y), dist)
    }

    fun minComponent(): N {
        return opSet.min(x, y)
    }

    fun maxComponent(): N {
        return opSet.max(x, y)
    }

    operator fun get(index: Int): N {
        return when (index) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException(index)
        }
    }

    operator fun get(axis: Direction.Axis): N {
        return when (axis) {
            Direction.Axis.X -> x
            Direction.Axis.Y -> y
            else -> throw IllegalDimensionException(axis.toString())
        }
    }

    fun anyGreaterThan(x: N, y: N): Boolean {
        return opSet.greaterThan(this.x, x) || opSet.greaterThan(this.y, y)
    }

    fun allGreaterThan(x: N, y: N): Boolean {
        return opSet.greaterThan(this.x, x) && opSet.greaterThan(this.y, y)
    }

    fun anyGequalThan(x: N, y: N): Boolean {
        return opSet.gequalThan(this.x, x) || opSet.gequalThan(this.y, y)
    }

    fun allGequalThan(x: N, y: N): Boolean {
        return opSet.gequalThan(this.x, x) && opSet.gequalThan(this.y, y)
    }

    fun anyLessThan(x: N, y: N): Boolean {
        return opSet.lessThan(this.x, x) || opSet.lessThan(this.y, y)
    }

    fun allLessThan(x: N, y: N): Boolean {
        return opSet.lessThan(this.x, x) && opSet.lessThan(this.y, y)
    }

    fun anyLequalThan(x: N, y: N): Boolean {
        return opSet.lequalThan(this.x, x) || opSet.lequalThan(this.y, y)
    }

    fun allLequalThan(x: N, y: N): Boolean {
        return opSet.lequalThan(this.x, x) && opSet.lequalThan(this.y, y)
    }

    fun lerp(other: IVec2<N>, d: Float) = lerp(other.x, other.y, d)

    fun lerp(x: N, d: Float) = lerp(x, x, d)

    operator fun plus(other: IVec2<N>) = plus(other.x, other.y)

    operator fun plus(x: N) = plus(x, x)

    operator fun minus(other: IVec2<N>) = minus(other.x, other.y)

    operator fun minus(x: N) = minus(x, x)

    operator fun times(other: IVec2<N>) = times(other.x, other.y)

    operator fun times(x: N) = times(x, x)

    operator fun div(other: IVec2<N>) = div(other.x, other.y)

    operator fun div(x: N) = div(x, x)

    operator fun rem(other: IVec2<N>) = rem(other.x, other.y)

    operator fun rem(x: N) = rem(x, x)

    fun min(other: IVec2<N>) = min(other.x, other.y)

    fun min(x: N) = min(x, x)

    fun max(other: IVec2<N>) = max(other.x, other.y)

    fun max(x: N) = max(x, x)

    fun distance(other: IVec2<N>) = distance(other.x, other.y)

    fun distanceSquared(other: IVec2<N>) = distanceSquared(other.x, other.y)

    fun gridDistance(other: IVec2<N>) = gridDistance(other.x, other.y)

    fun inDistance(other: IVec2<N>, dist: N) = inDistance(other.x, other.y, dist)

    fun inDistanceSquared(other: IVec2<N>, dist: N) = inDistanceSquared(other.x, other.y, dist)

    fun inGridDistance(other: IVec2<N>, dist: N) = inGridDistance(other.x, other.y, dist)

    fun anyGreaterThan(other: IVec2<N>) = anyGreaterThan(other.x, other.y)

    fun anyGreaterThan(x: N) = anyGreaterThan(x, x)

    fun allGreaterThan(other: IVec2<N>) = allGreaterThan(other.x, other.y)

    fun allGreaterThan(x: N) = allGreaterThan(x, x)

    fun anyGequalThan(other: IVec2<N>) = anyGequalThan(other.x, other.y)

    fun anyGequalThan(x: N) = anyGequalThan(x, x)

    fun allGequalThan(other: IVec2<N>) = allGequalThan(other.x, other.y)

    fun allGequalThan(x: N) = allGequalThan(x, x)

    fun anyLessThan(other: IVec2<N>) = anyLessThan(other.x, other.y)

    fun anyLessThan(x: N) = anyLessThan(x, x)

    fun allLessThan(other: IVec2<N>) = allLessThan(other.x, other.y)

    fun allLessThan(x: N) = allLessThan(x, x)

    fun anyLequalThan(other: IVec2<N>) = anyLequalThan(other.x, other.y)

    fun anyLequalThan(x: N) = anyLequalThan(x, x)

    fun allLequalThan(other: IVec2<N>) = allLequalThan(other.x, other.y)

    fun allLequalThan(x: N) = allLequalThan(x, x)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = copyWith(opSet.negate(x), opSet.negate(y))

    operator fun inc() = plus(opSet.one, opSet.one)

    operator fun dec() = minus(opSet.one, opSet.one)

    fun equals(x: N, y: N): Boolean {
        return this.x == x && this.y == y
    }

    fun equals(other: IVec2<N>): Boolean {
        return equals(other.x, other.y)
    }

    fun immutable() = this

    fun toJVec2i() = Vector2i(x.toInt(), y.toInt())

    fun toJVec2f() = Vector2f(x.toFloat(), y.toFloat())

    fun toJVec2d() = Vector2d(x.toDouble(), y.toDouble())

    private class IntImpl(
        override val x: Int,
        override val y: Int
    ) : IVec2<Int> {
        override val opSet: OperatorSet<Int>
            get() = IntOperatorSet

        override fun copyWith(x: Int, y: Int) = IVec2(x, y)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IVec3<*>) return false

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + y.hashCode()
            return result
        }

        override fun toString(): String {
            return "(x=$x, y=$y)"
        }
    }

    private class FloatImpl(
        override val x: Float,
        override val y: Float
    ) : IVec2<Float> {
        override val opSet: OperatorSet<Float>
            get() = FloatOperatorSet

        override fun copyWith(x: Float, y: Float) = IVec2(x, y)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IVec3<*>) return false

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + y.hashCode()
            return result
        }

        override fun toString(): String {
            return "(x=$x, y=$y)"
        }
    }

    private class DoubleImpl(
        override val x: Double,
        override val y: Double
    ) : IVec2<Double> {
        override val opSet: OperatorSet<Double>
            get() = DoubleOperatorSet

        override fun copyWith(x: Double, y: Double) = IVec2(x, y)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IVec3<*>) return false

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + y.hashCode()
            return result
        }

        override fun toString(): String {
            return "(x=$x, y=$y)"
        }
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<IVec2<Int>> = Codec.list(Codec.INT, 2, 2).xmap(
            { IVec2(it[0], it[1]) },
            { listOf(it.x, it.y) }
        )
        @JvmField
        val FLOAT_CODEC: Codec<IVec2<Float>> = Codec.list(Codec.FLOAT, 2, 2).xmap(
            { IVec2(it[0], it[1]) },
            { listOf(it.x, it.y) }
        )
        @JvmField
        val DOUBLE_CODEC: Codec<IVec2<Double>> = Codec.list(Codec.DOUBLE, 2, 2).xmap(
            { IVec2(it[0], it[1]) },
            { listOf(it.x, it.y) }
        )

        @JvmField
        val INT_STREAM_CODEC: StreamCodec<ByteBuf, IVec2<Int>> = StreamCodec.composite(
            ByteBufCodecs.INT, IVec2<Int>::x,
            ByteBufCodecs.INT, IVec2<Int>::y,
            ::invoke
        )
        @JvmField
        val FLOAT_STREAM_CODEC: StreamCodec<ByteBuf, IVec2<Float>> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, IVec2<Float>::x,
            ByteBufCodecs.FLOAT, IVec2<Float>::y,
            ::invoke
        )
        @JvmField
        val DOUBLE_STREAM_CODEC: StreamCodec<ByteBuf, IVec2<Double>> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, IVec2<Double>::x,
            ByteBufCodecs.DOUBLE, IVec2<Double>::y,
            ::invoke
        )

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Int, y: Int): IVec2<Int> = IntImpl(x, y)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(other: Vector3ic): IVec2<Int> = IntImpl(other.x(), other.y())

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Int): IVec2<Int> = IntImpl(x, x)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Float, y: Float): IVec2<Float> = FloatImpl(x, y)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(other: Vector3fc): IVec2<Float> = FloatImpl(other.x(), other.y())

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Float): IVec2<Float> = FloatImpl(x, x)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Double, y: Double): IVec2<Double> = DoubleImpl(x, y)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(other: Vector3dc): IVec2<Double> = DoubleImpl(other.x(), other.y())

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Double): IVec2<Double> = DoubleImpl(x, x)

        @JvmStatic
        inline fun <reified N : Number> Array<IVec2<N>>.flat(): Array<N> {
            return flatMap { listOf(it.x, it.y) }.toTypedArray()
        }
    }
}
