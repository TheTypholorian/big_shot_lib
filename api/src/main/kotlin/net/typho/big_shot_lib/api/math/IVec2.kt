package net.typho.big_shot_lib.api.math

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Direction
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.typho.big_shot_lib.api.error.IllegalDimensionException
import net.typho.big_shot_lib.api.math.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.FloatOperatorSet
import net.typho.big_shot_lib.api.math.IntOperatorSet
import net.typho.big_shot_lib.api.math.OperatorSet
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
        get() = IVec2.opSet.max(
            IVec2.opSet.abs(
                IVec2.x
            ), IVec2.opSet.abs(IVec2.y))
    val lengthSquared: N
        get() = IVec2.opSet.plus(
            IVec2.opSet.times(
                IVec2.x,
                IVec2.x
            ), IVec2.opSet.times(
                IVec2.y,
                IVec2.y
            ))
    val length: Float
        get() = IVec2.opSet.sqrt(IVec2.lengthSquared)
    val abs: IVec2<N>
        get() = IVec2.copyWith(
            IVec2.opSet.abs(
                IVec2.x
            ),
            IVec2.opSet.abs(IVec2.y)
        )

    fun copyWith(x: N, y: N): IVec2<N>

    fun toInt(): IVec2<Int> = IVec2(IVec2.x.toInt(), IVec2.y.toInt())

    fun toFloat(): IVec2<Float> = IVec2(IVec2.x.toFloat(), IVec2.y.toFloat())

    fun toDouble(): IVec2<Double> = IVec2(IVec2.x.toDouble(), IVec2.y.toDouble())

    fun lerp(x: N, y: N, d: Float): IVec2<N> {
        return IVec2.copyWith(
            IVec2.opSet.lerp(
                this.x,
                x,
                d
            ), IVec2.opSet.lerp(this.y, y, d)
        )
    }

    fun plus(x: N, y: N): IVec2<N> {
        return IVec2.copyWith(
            IVec2.opSet.plus(
                this.x,
                x
            ), IVec2.opSet.plus(this.y, y)
        )
    }

    fun minus(x: N, y: N): IVec2<N> {
        return IVec2.copyWith(
            IVec2.opSet.minus(
                this.x,
                x
            ), IVec2.opSet.minus(this.y, y)
        )
    }

    fun times(x: N, y: N): IVec2<N> {
        return IVec2.copyWith(
            IVec2.opSet.times(
                this.x,
                x
            ), IVec2.opSet.times(this.y, y)
        )
    }

    fun div(x: N, y: N): IVec2<N> {
        return IVec2.copyWith(
            IVec2.opSet.div(
                this.x,
                x
            ), IVec2.opSet.div(this.y, y)
        )
    }

    fun rem(x: N, y: N): IVec2<N> {
        return IVec2.copyWith(
            IVec2.opSet.rem(
                this.x,
                x
            ), IVec2.opSet.rem(this.y, y)
        )
    }

    fun min(x: N, y: N): IVec2<N> {
        return IVec2.copyWith(
            IVec2.opSet.min(
                this.x,
                x
            ), IVec2.opSet.min(this.y, y)
        )
    }

    fun max(x: N, y: N): IVec2<N> {
        return IVec2.copyWith(
            IVec2.opSet.max(
                this.x,
                x
            ), IVec2.opSet.max(this.y, y)
        )
    }

    fun distance(x: N, y: N): Float {
        return IVec2.minus(x, y).length
    }

    fun distanceSquared(x: N, y: N): N {
        return IVec2.minus(x, y).lengthSquared
    }

    fun gridDistance(x: N, y: N): N {
        return IVec2.minus(x, y).gridLength
    }

    fun inDistance(x: N, y: N, dist: N): Boolean {
        return IVec2.inDistanceSquared(
            x,
            y,
            IVec2.opSet.times(dist, dist)
        )
    }

    fun inDistanceSquared(x: N, y: N, dist: N): Boolean {
        return IVec2.opSet.lessThan(
            IVec2.distanceSquared(
                x,
                y
            ), dist)
    }

    fun inGridDistance(x: N, y: N, dist: N): Boolean {
        return IVec2.opSet.lessThan(
            IVec2.gridDistance(
                x,
                y
            ), dist)
    }

    fun minComponent(): N {
        return IVec2.opSet.min(
            IVec2.x,
            IVec2.y
        )
    }

    fun maxComponent(): N {
        return IVec2.opSet.max(
            IVec2.x,
            IVec2.y
        )
    }

    operator fun get(index: Int): N {
        return when (index) {
            0 -> IVec2.x
            1 -> IVec2.y
            else -> throw IndexOutOfBoundsException(index)
        }
    }

    operator fun get(axis: Direction.Axis): N {
        return when (axis) {
            Direction.Axis.X -> IVec2.x
            Direction.Axis.Y -> IVec2.y
            else -> throw IllegalDimensionException(axis.toString())
        }
    }

    fun anyGreaterThan(x: N, y: N): Boolean {
        return IVec2.opSet.greaterThan(this.x, x) || IVec2.opSet.greaterThan(this.y, y)
    }

    fun allGreaterThan(x: N, y: N): Boolean {
        return IVec2.opSet.greaterThan(this.x, x) && IVec2.opSet.greaterThan(this.y, y)
    }

    fun anyGequalThan(x: N, y: N): Boolean {
        return IVec2.opSet.gequalThan(this.x, x) || IVec2.opSet.gequalThan(this.y, y)
    }

    fun allGequalThan(x: N, y: N): Boolean {
        return IVec2.opSet.gequalThan(this.x, x) && IVec2.opSet.gequalThan(this.y, y)
    }

    fun anyLessThan(x: N, y: N): Boolean {
        return IVec2.opSet.lessThan(this.x, x) || IVec2.opSet.lessThan(this.y, y)
    }

    fun allLessThan(x: N, y: N): Boolean {
        return IVec2.opSet.lessThan(this.x, x) && IVec2.opSet.lessThan(this.y, y)
    }

    fun anyLequalThan(x: N, y: N): Boolean {
        return IVec2.opSet.lequalThan(this.x, x) || IVec2.opSet.lequalThan(this.y, y)
    }

    fun allLequalThan(x: N, y: N): Boolean {
        return IVec2.opSet.lequalThan(this.x, x) && IVec2.opSet.lequalThan(this.y, y)
    }

    fun lerp(other: IVec2<N>, d: Float) =
        IVec2.lerp(other.x, other.y, d)

    fun lerp(x: N, d: Float) = IVec2.lerp(x, x, d)

    operator fun plus(other: IVec2<N>) = IVec2.plus(other.x, other.y)

    operator fun plus(x: N) = IVec2.plus(x, x)

    operator fun minus(other: IVec2<N>) =
        IVec2.minus(other.x, other.y)

    operator fun minus(x: N) = IVec2.minus(x, x)

    operator fun times(other: IVec2<N>) =
        IVec2.times(other.x, other.y)

    operator fun times(x: N) = IVec2.times(x, x)

    operator fun div(other: IVec2<N>) = IVec2.div(other.x, other.y)

    operator fun div(x: N) = IVec2.div(x, x)

    operator fun rem(other: IVec2<N>) = IVec2.rem(other.x, other.y)

    operator fun rem(x: N) = IVec2.rem(x, x)

    fun min(other: IVec2<N>) = IVec2.min(other.x, other.y)

    fun min(x: N) = IVec2.min(x, x)

    fun max(other: IVec2<N>) = IVec2.max(other.x, other.y)

    fun max(x: N) = IVec2.max(x, x)

    fun distance(other: IVec2<N>) = IVec2.distance(other.x, other.y)

    fun distanceSquared(other: IVec2<N>) =
        IVec2.distanceSquared(other.x, other.y)

    fun gridDistance(other: IVec2<N>) =
        IVec2.gridDistance(other.x, other.y)

    fun inDistance(other: IVec2<N>, dist: N) =
        IVec2.inDistance(other.x, other.y, dist)

    fun inDistanceSquared(other: IVec2<N>, dist: N) =
        IVec2.inDistanceSquared(other.x, other.y, dist)

    fun inGridDistance(other: IVec2<N>, dist: N) =
        IVec2.inGridDistance(other.x, other.y, dist)

    fun anyGreaterThan(other: IVec2<N>) =
        IVec2.anyGreaterThan(other.x, other.y)

    fun anyGreaterThan(x: N) = IVec2.anyGreaterThan(x, x)

    fun allGreaterThan(other: IVec2<N>) =
        IVec2.allGreaterThan(other.x, other.y)

    fun allGreaterThan(x: N) = IVec2.allGreaterThan(x, x)

    fun anyGequalThan(other: IVec2<N>) =
        IVec2.anyGequalThan(other.x, other.y)

    fun anyGequalThan(x: N) = IVec2.anyGequalThan(x, x)

    fun allGequalThan(other: IVec2<N>) =
        IVec2.allGequalThan(other.x, other.y)

    fun allGequalThan(x: N) = IVec2.allGequalThan(x, x)

    fun anyLessThan(other: IVec2<N>) =
        IVec2.anyLessThan(other.x, other.y)

    fun anyLessThan(x: N) = IVec2.anyLessThan(x, x)

    fun allLessThan(other: IVec2<N>) =
        IVec2.allLessThan(other.x, other.y)

    fun allLessThan(x: N) = IVec2.allLessThan(x, x)

    fun anyLequalThan(other: IVec2<N>) =
        IVec2.anyLequalThan(other.x, other.y)

    fun anyLequalThan(x: N) = IVec2.anyLequalThan(x, x)

    fun allLequalThan(other: IVec2<N>) =
        IVec2.allLequalThan(other.x, other.y)

    fun allLequalThan(x: N) = IVec2.allLequalThan(x, x)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = IVec2.copyWith(
        IVec2.opSet.negate(IVec2.x),
        IVec2.opSet.negate(IVec2.y)
    )

    operator fun inc() = IVec2.plus(
        IVec2.opSet.one,
        IVec2.opSet.one
    )

    operator fun dec() = IVec2.minus(
        IVec2.opSet.one,
        IVec2.opSet.one
    )

    fun equals(x: N, y: N): Boolean {
        return this.x == x && this.y == y
    }

    fun equals(other: IVec2<N>): Boolean {
        return IVec2.equals(other.x, other.y)
    }

    fun immutable() = this

    fun toJVec2i() = Vector2i(IVec2.x.toInt(), IVec2.y.toInt())

    fun toJVec2f() = Vector2f(IVec2.x.toFloat(), IVec2.y.toFloat())

    fun toJVec2d() = Vector2d(IVec2.x.toDouble(), IVec2.y.toDouble())

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
            ByteBufCodecs.INT, IVec2::x,
            ByteBufCodecs.INT, IVec2::y,
            ::invoke
        )
        @JvmField
        val FLOAT_STREAM_CODEC: StreamCodec<ByteBuf, IVec2<Float>> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, IVec2::x,
            ByteBufCodecs.FLOAT, IVec2::y,
            ::invoke
        )
        @JvmField
        val DOUBLE_STREAM_CODEC: StreamCodec<ByteBuf, IVec2<Double>> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, IVec2::x,
            ByteBufCodecs.DOUBLE, IVec2::y,
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
