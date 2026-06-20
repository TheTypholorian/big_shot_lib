package net.typho.big_shot_lib.api.math

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.joml.Vector4d
import org.joml.Vector4dc
import org.joml.Vector4f
import org.joml.Vector4fc
import org.joml.Vector4i
import org.joml.Vector4ic

interface IVec4<N : Number> {
    val opSet: OperatorSet<N>

    val x: N
    val y: N
    val z: N
    val w: N

    val gridLength: N
        get() = opSet.max(opSet.abs(x), opSet.max(opSet.abs(y), opSet.max(opSet.abs(z), opSet.abs(w))))
    val lengthSquared: N
        get() = opSet.plus(opSet.times(x, x), opSet.plus(opSet.times(y, y), opSet.plus(opSet.times(z, z), opSet.times(w, w))))
    val length: Float
        get() = opSet.sqrt(lengthSquared)
    val abs: IVec4<N>
        get() = copyWith(opSet.abs(x), opSet.abs(y), opSet.abs(z), opSet.abs(w))
    val xy: IVec2<N>
    val yz: IVec2<N>
    val zw: IVec2<N>
    val xyz: IVec3<N>

    val r: N
        get() = x
    val g: N
        get() = y
    val b: N
        get() = z
    val a: N
        get() = w

    val rg: IVec2<N>
    val gb: IVec2<N>
    val ba: IVec2<N>
    val rgb: IVec3<N>

    fun copyWith(x: N, y: N, z: N, w: N): IVec4<N>

    fun toInt(): IVec4<Int> = IVec4(x.toInt(), y.toInt(), z.toInt(), w.toInt())

    fun toFloat(): IVec4<Float> = IVec4(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

    fun toDouble(): IVec4<Double> = IVec4(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())

    fun lerp(x: N, y: N, z: N, w: N, d: Float): IVec4<N> {
        return copyWith(opSet.lerp(this.x, x, d), opSet.lerp(this.y, y, d), opSet.lerp(this.z, z, d), opSet.lerp(this.w, w, d))
    }

    fun plus(x: N, y: N, z: N, w: N): IVec4<N> {
        return copyWith(opSet.plus(this.x, x), opSet.plus(this.y, y), opSet.plus(this.z, z), opSet.plus(this.w, w))
    }

    fun minus(x: N, y: N, z: N, w: N): IVec4<N> {
        return copyWith(opSet.minus(this.x, x), opSet.minus(this.y, y), opSet.minus(this.z, z), opSet.minus(this.w, w))
    }

    fun times(x: N, y: N, z: N, w: N): IVec4<N> {
        return copyWith(opSet.times(this.x, x), opSet.times(this.y, y), opSet.times(this.z, z), opSet.times(this.w, w))
    }

    fun div(x: N, y: N, z: N, w: N): IVec4<N> {
        return copyWith(opSet.div(this.x, x), opSet.div(this.y, y), opSet.div(this.z, z), opSet.div(this.w, w))
    }

    fun rem(x: N, y: N, z: N, w: N): IVec4<N> {
        return copyWith(opSet.rem(this.x, x), opSet.rem(this.y, y), opSet.rem(this.z, z), opSet.rem(this.w, w))
    }

    fun min(x: N, y: N, z: N, w: N): IVec4<N> {
        return copyWith(opSet.min(this.x, x), opSet.min(this.y, y), opSet.min(this.z, z), opSet.min(this.w, w))
    }

    fun max(x: N, y: N, z: N, w: N): IVec4<N> {
        return copyWith(opSet.max(this.x, x), opSet.max(this.y, y), opSet.max(this.z, z), opSet.max(this.w, w))
    }

    fun distance(x: N, y: N, z: N, w: N): Float {
        return minus(x, y, z, w).length
    }

    fun distanceSquared(x: N, y: N, z: N, w: N): N {
        return minus(x, y, z, w).lengthSquared
    }

    fun gridDistance(x: N, y: N, z: N, w: N): N {
        return minus(x, y, z, w).gridLength
    }

    fun inDistance(x: N, y: N, z: N, w: N, dist: N): Boolean {
        return inDistanceSquared(x, y, z, w, opSet.times(dist, dist))
    }

    fun inDistanceSquared(x: N, y: N, z: N, w: N, dist: N): Boolean {
        return opSet.lessThan(distanceSquared(x, y, z, w), dist)
    }

    fun inGridDistance(x: N, y: N, z: N, w: N, dist: N): Boolean {
        return opSet.lessThan(gridDistance(x, y, z, w), dist)
    }

    fun minComponent(): N {
        return opSet.min(x, opSet.min(y, opSet.min(z, w)))
    }

    fun maxComponent(): N {
        return opSet.max(x, opSet.max(y, opSet.max(z, w)))
    }

    operator fun get(index: Int): N {
        return when (index) {
            0 -> x
            1 -> y
            2 -> z
            3 -> w
            else -> throw IndexOutOfBoundsException(index)
        }
    }

    fun anyGreaterThan(x: N, y: N, z: N, w: N): Boolean {
        return opSet.greaterThan(this.x, x) || opSet.greaterThan(this.y, y) || opSet.greaterThan(this.z, z) || opSet.greaterThan(this.w, w)
    }

    fun allGreaterThan(x: N, y: N, z: N, w: N): Boolean {
        return opSet.greaterThan(this.x, x) && opSet.greaterThan(this.y, y) && opSet.greaterThan(this.z, z) && opSet.greaterThan(this.w, w)
    }

    fun anyGequalThan(x: N, y: N, z: N, w: N): Boolean {
        return opSet.gequalThan(this.x, x) || opSet.gequalThan(this.y, y) || opSet.gequalThan(this.z, z) || opSet.gequalThan(this.w, w)
    }

    fun allGequalThan(x: N, y: N, z: N, w: N): Boolean {
        return opSet.gequalThan(this.x, x) && opSet.gequalThan(this.y, y) && opSet.gequalThan(this.z, z) && opSet.gequalThan(this.w, w)
    }

    fun anyLessThan(x: N, y: N, z: N, w: N): Boolean {
        return opSet.lessThan(this.x, x) || opSet.lessThan(this.y, y) || opSet.lessThan(this.z, z) || opSet.lessThan(this.w, w)
    }

    fun allLessThan(x: N, y: N, z: N, w: N): Boolean {
        return opSet.lessThan(this.x, x) && opSet.lessThan(this.y, y) && opSet.lessThan(this.z, z) && opSet.lessThan(this.w, w)
    }

    fun anyLequalThan(x: N, y: N, z: N, w: N): Boolean {
        return opSet.lequalThan(this.x, x) || opSet.lequalThan(this.y, y) || opSet.lequalThan(this.z, z) || opSet.lequalThan(this.w, w)
    }

    fun allLequalThan(x: N, y: N, z: N, w: N): Boolean {
        return opSet.lequalThan(this.x, x) && opSet.lequalThan(this.y, y) && opSet.lequalThan(this.z, z) && opSet.lequalThan(this.w, w)
    }

    fun lerp(other: IVec4<N>, d: Float) = lerp(other.x, other.y, other.z, other.w, d)

    fun lerp(x: N, d: Float) = lerp(x, x, x, x, d)

    operator fun plus(other: IVec4<N>) = plus(other.x, other.y, other.z, other.w)

    operator fun plus(x: N) = plus(x, x, x, x)

    operator fun minus(other: IVec4<N>) = minus(other.x, other.y, other.z, other.w)

    operator fun minus(x: N) = minus(x, x, x, x)

    operator fun times(other: IVec4<N>) = times(other.x, other.y, other.z, other.w)

    operator fun times(x: N) = times(x, x, x, x)

    operator fun div(other: IVec4<N>) = div(other.x, other.y, other.z, other.w)

    operator fun div(x: N) = div(x, x, x, x)

    operator fun rem(other: IVec4<N>) = rem(other.x, other.y, other.z, other.w)

    operator fun rem(x: N) = rem(x, x, x, x)

    fun min(other: IVec4<N>) = min(other.x, other.y, other.z, other.w)

    fun min(x: N) = min(x, x, x, x)

    fun max(other: IVec4<N>) = max(other.x, other.y, other.z, other.w)

    fun max(x: N) = max(x, x, x, x)

    fun distance(other: IVec4<N>) = distance(other.x, other.y, other.z, other.w)

    fun distanceSquared(other: IVec4<N>) = distanceSquared(other.x, other.y, other.z, other.w)

    fun gridDistance(other: IVec4<N>) = gridDistance(other.x, other.y, other.z, other.w)

    fun inDistance(other: IVec4<N>, dist: N) = inDistance(other.x, other.y, other.z, other.w, dist)

    fun inDistanceSquared(other: IVec4<N>, dist: N) = inDistanceSquared(other.x, other.y, other.z, other.w, dist)

    fun inGridDistance(other: IVec4<N>, dist: N) = inGridDistance(other.x, other.y, other.z, other.w, dist)

    fun anyGreaterThan(other: IVec4<N>) = anyGreaterThan(other.x, other.y, other.z, other.w)

    fun anyGreaterThan(x: N) = anyGreaterThan(x, x, x, x)

    fun allGreaterThan(other: IVec4<N>) = allGreaterThan(other.x, other.y, other.z, other.w)

    fun allGreaterThan(x: N) = allGreaterThan(x, x, x, x)

    fun anyGequalThan(other: IVec4<N>) = anyGequalThan(other.x, other.y, other.z, other.w)

    fun anyGequalThan(x: N) = anyGequalThan(x, x, x, x)

    fun allGequalThan(other: IVec4<N>) = allGequalThan(other.x, other.y, other.z, other.w)

    fun allGequalThan(x: N) = allGequalThan(x, x, x, x)

    fun anyLessThan(other: IVec4<N>) = anyLessThan(other.x, other.y, other.z, other.w)

    fun anyLessThan(x: N) = anyLessThan(x, x, x, x)

    fun allLessThan(other: IVec4<N>) = allLessThan(other.x, other.y, other.z, other.w)

    fun allLessThan(x: N) = allLessThan(x, x, x, x)

    fun anyLequalThan(other: IVec4<N>) = anyLequalThan(other.x, other.y, other.z, other.w)

    fun anyLequalThan(x: N) = anyLequalThan(x, x, x, x)

    fun allLequalThan(other: IVec4<N>) = allLequalThan(other.x, other.y, other.z, other.w)

    fun allLequalThan(x: N) = allLequalThan(x, x, x, x)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = copyWith(opSet.negate(x), opSet.negate(y), opSet.negate(z), opSet.negate(w))

    operator fun inc() = plus(opSet.one, opSet.one, opSet.one, opSet.one)

    operator fun dec() = minus(opSet.one, opSet.one, opSet.one, opSet.one)

    fun equals(x: N, y: N, z: N, w: N): Boolean {
        return this.x == x && this.y == y && this.z == z && this.w == w
    }

    fun equals(other: IVec4<N>): Boolean {
        return equals(other.x, other.y, other.z, other.w)
    }

    fun immutable() = this

    fun toJVec4i() = Vector4i(x.toInt(), y.toInt(), z.toInt(), w.toInt())

    fun toJVec4f() = Vector4f(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

    fun toJVec4d() = Vector4d(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())

    private class IntImpl(
        override val x: Int,
        override val y: Int,
        override val z: Int,
        override val w: Int
    ) : IVec4<Int> {
        override val opSet: OperatorSet<Int>
            get() = IntOperatorSet
        override val xy: IVec2<Int>
            get() = IVec2(x, y)
        override val yz: IVec2<Int>
            get() = IVec2(y, z)
        override val zw: IVec2<Int>
            get() = IVec2(z, w)
        override val xyz: IVec3<Int>
            get() = IVec3(x, y, z)
        override val rg: IVec2<Int>
            get() = IVec2(r, g)
        override val gb: IVec2<Int>
            get() = IVec2(g, b)
        override val ba: IVec2<Int>
            get() = IVec2(b, a)
        override val rgb: IVec3<Int>
            get() = IVec3(r, g, b)

        override fun copyWith(x: Int, y: Int, z: Int, w: Int) = IVec4(x, y, z, w)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IVec4<*>) return false

            if (x != other.x) return false
            if (y != other.y) return false
            if (z != other.z) return false
            if (w != other.w) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 41 * result + y.hashCode()
            result = 41 * result + z.hashCode()
            result = 41 * result + w.hashCode()
            return result
        }

        override fun toString(): String {
            return "(x=$x, y=$y, z=$z, w=$w)"
        }
    }

    private class FloatImpl(
        override val x: Float,
        override val y: Float,
        override val z: Float,
        override val w: Float
    ) : IVec4<Float> {
        override val opSet: OperatorSet<Float>
            get() = FloatOperatorSet
        override val xy: IVec2<Float>
            get() = IVec2(x, y)
        override val yz: IVec2<Float>
            get() = IVec2(y, z)
        override val zw: IVec2<Float>
            get() = IVec2(z, w)
        override val xyz: IVec3<Float>
            get() = IVec3(x, y, z)
        override val rg: IVec2<Float>
            get() = IVec2(r, g)
        override val gb: IVec2<Float>
            get() = IVec2(g, b)
        override val ba: IVec2<Float>
            get() = IVec2(b, a)
        override val rgb: IVec3<Float>
            get() = IVec3(r, g, b)

        override fun copyWith(x: Float, y: Float, z: Float, w: Float) = IVec4(x, y, z, w)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IVec4<*>) return false

            if (x != other.x) return false
            if (y != other.y) return false
            if (z != other.z) return false
            if (w != other.w) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 41 * result + y.hashCode()
            result = 41 * result + z.hashCode()
            result = 41 * result + w.hashCode()
            return result
        }

        override fun toString(): String {
            return "(x=$x, y=$y, z=$z, w=$w)"
        }
    }

    private class DoubleImpl(
        override val x: Double,
        override val y: Double,
        override val z: Double,
        override val w: Double
    ) : IVec4<Double> {
        override val opSet: OperatorSet<Double>
            get() = DoubleOperatorSet
        override val xy: IVec2<Double>
            get() = IVec2(x, y)
        override val yz: IVec2<Double>
            get() = IVec2(y, z)
        override val zw: IVec2<Double>
            get() = IVec2(z, w)
        override val xyz: IVec3<Double>
            get() = IVec3(x, y, z)
        override val rg: IVec2<Double>
            get() = IVec2(r, g)
        override val gb: IVec2<Double>
            get() = IVec2(g, b)
        override val ba: IVec2<Double>
            get() = IVec2(b, a)
        override val rgb: IVec3<Double>
            get() = IVec3(r, g, b)

        override fun copyWith(x: Double, y: Double, z: Double, w: Double) = IVec4(x, y, z, w)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IVec4<*>) return false

            if (x != other.x) return false
            if (y != other.y) return false
            if (z != other.z) return false
            if (w != other.w) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 41 * result + y.hashCode()
            result = 41 * result + z.hashCode()
            result = 41 * result + w.hashCode()
            return result
        }

        override fun toString(): String {
            return "(x=$x, y=$y, z=$z, w=$w)"
        }
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<IVec4<Int>> = Codec.list(Codec.INT, 4, 4).xmap(
            { IVec4(it[0], it[1], it[2], it[3]) },
            { listOf(it.x, it.y, it.z, it.w) })
        @JvmField
        val FLOAT_CODEC: Codec<IVec4<Float>> = Codec.list(Codec.FLOAT, 4, 4).xmap(
            { IVec4(it[0], it[1], it[2], it[3]) },
            { listOf(it.x, it.y, it.z, it.w) })
        @JvmField
        val DOUBLE_CODEC: Codec<IVec4<Double>> = Codec.list(Codec.DOUBLE, 4, 4).xmap(
            { IVec4(it[0], it[1], it[2], it[3]) },
            { listOf(it.x, it.y, it.z, it.w) })

        @JvmField
        val INT_STREAM_CODEC: StreamCodec<ByteBuf, IVec4<Int>> = StreamCodec.composite(
            ByteBufCodecs.INT, IVec4<Int>::x,
            ByteBufCodecs.INT, IVec4<Int>::y,
            ByteBufCodecs.INT, IVec4<Int>::z,
            ByteBufCodecs.INT, IVec4<Int>::w,
            ::invoke
        )
        @JvmField
        val FLOAT_STREAM_CODEC: StreamCodec<ByteBuf, IVec4<Float>> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, IVec4<Float>::x,
            ByteBufCodecs.FLOAT, IVec4<Float>::y,
            ByteBufCodecs.FLOAT, IVec4<Float>::z,
            ByteBufCodecs.FLOAT, IVec4<Float>::w,
            ::invoke
        )
        @JvmField
        val DOUBLE_STREAM_CODEC: StreamCodec<ByteBuf, IVec4<Double>> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, IVec4<Double>::x,
            ByteBufCodecs.DOUBLE, IVec4<Double>::y,
            ByteBufCodecs.DOUBLE, IVec4<Double>::z,
            ByteBufCodecs.DOUBLE, IVec4<Double>::w,
            ::invoke
        )

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Int, y: Int, z: Int, w: Int): IVec4<Int> = IntImpl(x, y, z, w)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(other: Vector4ic): IVec4<Int> = IntImpl(other.x(), other.y(), other.z(), other.w())

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Int): IVec4<Int> = IntImpl(x, x, x, x)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Float, y: Float, z: Float, w: Float): IVec4<Float> = FloatImpl(x, y, z, w)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(other: Vector4fc): IVec4<Float> = FloatImpl(other.x(), other.y(), other.z(), other.w())

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Float): IVec4<Float> = FloatImpl(x, x, x, x)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Double, y: Double, z: Double, w: Double): IVec4<Double> = DoubleImpl(x, y, z, w)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(other: Vector4dc): IVec4<Double> = DoubleImpl(other.x(), other.y(), other.z(), other.w())

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Double): IVec4<Double> = DoubleImpl(x, x, x, x)

        @JvmStatic
        inline fun <reified N : Number> Array<IVec4<N>>.flat(): Array<N> {
            return flatMap { listOf(it.x, it.y, it.z, it.w) }.toTypedArray()
        }
    }
}
