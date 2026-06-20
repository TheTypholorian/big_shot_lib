package net.typho.big_shot_lib.api.math

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3
import net.typho.big_shot_lib.api.math.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.FloatOperatorSet
import net.typho.big_shot_lib.api.math.IntOperatorSet
import net.typho.big_shot_lib.api.math.OperatorSet
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3f
import org.joml.Vector3fc
import org.joml.Vector3i
import org.joml.Vector3ic
import org.joml.Vector4d
import org.joml.Vector4f
import org.joml.Vector4i

interface IVec3<N : Number> {
    val opSet: OperatorSet<N>

    val x: N
    val y: N
    val z: N

    val gridLength: N
        get() = opSet.max(opSet.abs(x), opSet.max(opSet.abs(y), opSet.abs(z)))
    val lengthSquared: N
        get() = opSet.plus(opSet.times(x, x), opSet.plus(opSet.times(y, y), opSet.times(z, z)))
    val length: Float
        get() = opSet.sqrt(lengthSquared)
    val abs: IVec3<N>
        get() = copyWith(opSet.abs(x), opSet.abs(y), opSet.abs(z))
    val xy: IVec2<N>
    val yz: IVec2<N>
    val xz: IVec2<N>

    val r: N
        get() = x
    val g: N
        get() = y
    val b: N
        get() = z

    val rg: IVec2<N>
        get() = xy
    val gb: IVec2<N>
        get() = yz
    val rb: IVec2<N>
        get() = xz

    fun copyWith(x: N, y: N, z: N): IVec3<N>

    fun toInt(): IVec3<Int> = IntImpl(x.toInt(), y.toInt(), z.toInt())

    fun toFloat(): IVec3<Float> = IVec3(x.toFloat(), y.toFloat(), z.toFloat())

    fun toDouble(): IVec3<Double> = IVec3(x.toDouble(), y.toDouble(), z.toDouble())

    fun lerp(x: N, y: N, z: N, d: Float): IVec3<N> {
        return copyWith(opSet.lerp(this.x, x, d), opSet.lerp(this.y, y, d), opSet.lerp(this.z, z, d))
    }

    fun plus(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.plus(this.x, x), opSet.plus(this.y, y), opSet.plus(this.z, z))
    }

    fun minus(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.minus(this.x, x), opSet.minus(this.y, y), opSet.minus(this.z, z))
    }

    fun times(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.times(this.x, x), opSet.times(this.y, y), opSet.times(this.z, z))
    }

    fun div(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.div(this.x, x), opSet.div(this.y, y), opSet.div(this.z, z))
    }

    fun rem(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.rem(this.x, x), opSet.rem(this.y, y), opSet.rem(this.z, z))
    }

    fun cross(x: N, y: N, z: N): IVec3<N> {
        return copyWith(
            opSet.plus(opSet.times(this.y, z), opSet.times(opSet.negate(this.z), y)),
            opSet.plus(opSet.times(this.z, x), opSet.times(opSet.negate(this.x), z)),
            opSet.plus(opSet.times(this.x, y), opSet.times(opSet.negate(this.y), x))
        )
    }

    fun min(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.min(this.x, x), opSet.min(this.y, y), opSet.min(this.z, z))
    }

    fun max(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.max(this.x, x), opSet.max(this.y, y), opSet.max(this.z, z))
    }

    fun distance(x: N, y: N, z: N): Float {
        return minus(x, y, z).length
    }

    fun distanceSquared(x: N, y: N, z: N): N {
        return minus(x, y, z).lengthSquared
    }

    fun gridDistance(x: N, y: N, z: N): N {
        return minus(x, y, z).gridLength
    }

    fun inDistance(x: N, y: N, z: N, dist: N): Boolean {
        return inDistanceSquared(x, y, z, opSet.times(dist, dist))
    }

    fun inDistanceSquared(x: N, y: N, z: N, dist: N): Boolean {
        return opSet.lessThan(distanceSquared(x, y, z), dist)
    }

    fun inGridDistance(x: N, y: N, z: N, dist: N): Boolean {
        return opSet.lessThan(gridDistance(x, y, z), dist)
    }

    fun minComponent(): N {
        return opSet.min(x, opSet.min(y, z))
    }

    fun maxComponent(): N {
        return opSet.max(x, opSet.max(y, z))
    }

    operator fun get(index: Int): N {
        return when (index) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IndexOutOfBoundsException(index)
        }
    }

    operator fun get(axis: Direction.Axis): N {
        return when (axis) {
            Direction.Axis.X -> x
            Direction.Axis.Y -> y
            Direction.Axis.Z -> z
        }
    }

    fun anyGreaterThan(x: N, y: N, z: N): Boolean {
        return opSet.greaterThan(this.x, x) || opSet.greaterThan(this.y, y) || opSet.greaterThan(this.z, z)
    }

    fun allGreaterThan(x: N, y: N, z: N): Boolean {
        return opSet.greaterThan(this.x, x) && opSet.greaterThan(this.y, y) && opSet.greaterThan(this.z, z)
    }

    fun anyGequalThan(x: N, y: N, z: N): Boolean {
        return opSet.gequalThan(this.x, x) || opSet.gequalThan(this.y, y) || opSet.gequalThan(this.z, z)
    }

    fun allGequalThan(x: N, y: N, z: N): Boolean {
        return opSet.gequalThan(this.x, x) && opSet.gequalThan(this.y, y) && opSet.gequalThan(this.z, z)
    }

    fun anyLessThan(x: N, y: N, z: N): Boolean {
        return opSet.lessThan(this.x, x) || opSet.lessThan(this.y, y) || opSet.lessThan(this.z, z)
    }

    fun allLessThan(x: N, y: N, z: N): Boolean {
        return opSet.lessThan(this.x, x) && opSet.lessThan(this.y, y) && opSet.lessThan(this.z, z)
    }

    fun anyLequalThan(x: N, y: N, z: N): Boolean {
        return opSet.lequalThan(this.x, x) || opSet.lequalThan(this.y, y) || opSet.lequalThan(this.z, z)
    }

    fun allLequalThan(x: N, y: N, z: N): Boolean {
        return opSet.lequalThan(this.x, x) && opSet.lequalThan(this.y, y) && opSet.lequalThan(this.z, z)
    }

    fun lerp(other: IVec3<N>, d: Float) = lerp(other.x, other.y, other.z, d)

    fun lerp(x: N, d: Float) = lerp(x, x, x, d)

    operator fun plus(other: IVec3<N>) = plus(other.x, other.y, other.z)

    operator fun plus(x: N) = plus(x, x, x)

    operator fun minus(other: IVec3<N>) = minus(other.x, other.y, other.z)

    operator fun minus(x: N) = minus(x, x, x)

    operator fun times(other: IVec3<N>) = times(other.x, other.y, other.z)

    operator fun times(x: N) = times(x, x, x)

    operator fun div(other: IVec3<N>) = div(other.x, other.y, other.z)

    operator fun div(x: N) = div(x, x, x)

    operator fun rem(other: IVec3<N>) = rem(other.x, other.y, other.z)

    operator fun rem(x: N) = rem(x, x, x)

    infix fun cross(other: IVec3<N>) = cross(other.x, other.y, other.z)

    infix fun cross(x: N) = cross(x, x, x)

    fun min(other: IVec3<N>) = min(other.x, other.y, other.z)

    fun min(x: N) = min(x, x, x)

    fun max(other: IVec3<N>) = max(other.x, other.y, other.z)

    fun max(x: N) = max(x, x, x)

    fun distance(other: IVec3<N>) = distance(other.x, other.y, other.z)

    fun distanceSquared(other: IVec3<N>) = distanceSquared(other.x, other.y, other.z)

    fun gridDistance(other: IVec3<N>) = gridDistance(other.x, other.y, other.z)

    fun inDistance(other: IVec3<N>, dist: N) = inDistance(other.x, other.y, other.z, dist)

    fun inDistanceSquared(other: IVec3<N>, dist: N) = inDistanceSquared(other.x, other.y, other.z, dist)

    fun inGridDistance(other: IVec3<N>, dist: N) = inGridDistance(other.x, other.y, other.z, dist)

    fun anyGreaterThan(other: IVec3<N>) = anyGreaterThan(other.x, other.y, other.z)

    fun anyGreaterThan(x: N) = anyGreaterThan(x, x, x)

    fun allGreaterThan(other: IVec3<N>) = allGreaterThan(other.x, other.y, other.z)

    fun allGreaterThan(x: N) = allGreaterThan(x, x, x)

    fun anyGequalThan(other: IVec3<N>) = anyGequalThan(other.x, other.y, other.z)

    fun anyGequalThan(x: N) = anyGequalThan(x, x, x)

    fun allGequalThan(other: IVec3<N>) = allGequalThan(other.x, other.y, other.z)

    fun allGequalThan(x: N) = allGequalThan(x, x, x)

    fun anyLessThan(other: IVec3<N>) = anyLessThan(other.x, other.y, other.z)

    fun anyLessThan(x: N) = anyLessThan(x, x, x)

    fun allLessThan(other: IVec3<N>) = allLessThan(other.x, other.y, other.z)

    fun allLessThan(x: N) = allLessThan(x, x, x)

    fun anyLequalThan(other: IVec3<N>) = anyLequalThan(other.x, other.y, other.z)

    fun anyLequalThan(x: N) = anyLequalThan(x, x, x)

    fun allLequalThan(other: IVec3<N>) = allLequalThan(other.x, other.y, other.z)

    fun allLequalThan(x: N) = allLequalThan(x, x, x)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = copyWith(opSet.negate(x), opSet.negate(y), opSet.negate(z))

    operator fun inc() = plus(opSet.one, opSet.one, opSet.one)

    operator fun dec() = minus(opSet.one, opSet.one, opSet.one)

    fun equals(x: N, y: N, z: N): Boolean {
        return this.x == x && this.y == y && this.z == z
    }

    fun equals(other: IVec3<N>): Boolean {
        return equals(other.x, other.y, other.z)
    }

    fun immutable() = this

    fun toBlockPos() = BlockPos(x.toInt(), y.toInt(), z.toInt())

    fun toVec3i(): Vec3i = toBlockPos()

    fun toVec3() = Vec3(x.toDouble(), y.toDouble(), z.toDouble())

    fun toJVec3i() = Vector3i(x.toInt(), y.toInt(), z.toInt())

    fun toJVec3f() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

    fun toJVec3d() = Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

    fun toJVec4i(w: Int) = Vector4i(x.toInt(), y.toInt(), z.toInt(), w)

    fun toJVec4f(w: Float) = Vector4f(x.toFloat(), y.toFloat(), z.toFloat(), w)

    fun toJVec4d(w: Double) = Vector4d(x.toDouble(), y.toDouble(), z.toDouble(), w)

    private class IntImpl(
        override val x: Int,
        override val y: Int,
        override val z: Int
    ) : IVec3<Int> {
        override val opSet: OperatorSet<Int>
            get() = IntOperatorSet
        override val xy: IVec2<Int>
            get() = IVec2(x, y)
        override val yz: IVec2<Int>
            get() = IVec2(y, z)
        override val xz: IVec2<Int>
            get() = IVec2(x, z)

        override fun copyWith(x: Int, y: Int, z: Int) = IVec3(x, y, z)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IVec3<*>) return false

            if (x != other.x) return false
            if (y != other.y) return false
            if (z != other.z) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + y.hashCode()
            result = 31 * result + z.hashCode()
            return result
        }

        override fun toString(): String {
            return "(x=$x, y=$y, z=$z)"
        }
    }

    private class FloatImpl(
        override val x: Float,
        override val y: Float,
        override val z: Float
    ) : IVec3<Float> {
        override val opSet: OperatorSet<Float>
            get() = FloatOperatorSet
        override val xy: IVec2<Float>
            get() = IVec2(x, y)
        override val yz: IVec2<Float>
            get() = IVec2(y, z)
        override val xz: IVec2<Float>
            get() = IVec2(x, z)

        override fun copyWith(x: Float, y: Float, z: Float) = IVec3(x, y, z)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IVec3<*>) return false

            if (x != other.x) return false
            if (y != other.y) return false
            if (z != other.z) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + y.hashCode()
            result = 31 * result + z.hashCode()
            return result
        }

        override fun toString(): String {
            return "(x=$x, y=$y, z=$z)"
        }
    }

    private class DoubleImpl(
        override val x: Double,
        override val y: Double,
        override val z: Double
    ) : IVec3<Double> {
        override val opSet: OperatorSet<Double>
            get() = DoubleOperatorSet
        override val xy: IVec2<Double>
            get() = IVec2(x, y)
        override val yz: IVec2<Double>
            get() = IVec2(y, z)
        override val xz: IVec2<Double>
            get() = IVec2(x, z)

        override fun copyWith(x: Double, y: Double, z: Double) = IVec3(x, y, z)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IVec3<*>) return false

            if (x != other.x) return false
            if (y != other.y) return false
            if (z != other.z) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + y.hashCode()
            result = 31 * result + z.hashCode()
            return result
        }

        override fun toString(): String {
            return "(x=$x, y=$y, z=$z)"
        }
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<IVec3<Int>> = Codec.list(Codec.INT, 3, 3).xmap(
            { IVec3(it[0], it[1], it[2]) },
            { listOf(it.x, it.y, it.z) }
        )
        @JvmField
        val FLOAT_CODEC: Codec<IVec3<Float>> = Codec.list(Codec.FLOAT, 3, 3).xmap(
            { IVec3(it[0], it[1], it[2]) },
            { listOf(it.x, it.y, it.z) }
        )
        @JvmField
        val DOUBLE_CODEC: Codec<IVec3<Double>> = Codec.list(Codec.DOUBLE, 3, 3).xmap(
            { IVec3(it[0], it[1], it[2]) },
            { listOf(it.x, it.y, it.z) }
        )

        @JvmField
        val INT_STREAM_CODEC: StreamCodec<ByteBuf, IVec3<Int>> = StreamCodec.composite(
            ByteBufCodecs.INT, IVec3<Int>::x,
            ByteBufCodecs.INT, IVec3<Int>::y,
            ByteBufCodecs.INT, IVec3<Int>::z,
            ::invoke
        )
        @JvmField
        val FLOAT_STREAM_CODEC: StreamCodec<ByteBuf, IVec3<Float>> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, IVec3<Float>::x,
            ByteBufCodecs.FLOAT, IVec3<Float>::y,
            ByteBufCodecs.FLOAT, IVec3<Float>::z,
            ::invoke
        )
        @JvmField
        val DOUBLE_STREAM_CODEC: StreamCodec<ByteBuf, IVec3<Double>> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, IVec3<Double>::x,
            ByteBufCodecs.DOUBLE, IVec3<Double>::y,
            ByteBufCodecs.DOUBLE, IVec3<Double>::z,
            ::invoke
        )

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Int, y: Int, z: Int): IVec3<Int> = IntImpl(x, y, z)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(other: Vector3ic): IVec3<Int> = IntImpl(other.x(), other.y(), other.z())

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Int): IVec3<Int> = IntImpl(x, x, x)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Float, y: Float, z: Float): IVec3<Float> = FloatImpl(x, y, z)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(other: Vector3fc): IVec3<Float> = FloatImpl(other.x(), other.y(), other.z())

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Float): IVec3<Float> = FloatImpl(x, x, x)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Double, y: Double, z: Double): IVec3<Double> = DoubleImpl(x, y, z)

        @JvmStatic
        @JvmName("of")
        operator fun invoke(other: Vector3dc): IVec3<Double> = DoubleImpl(other.x(), other.y(), other.z())

        @JvmStatic
        @JvmName("of")
        operator fun invoke(x: Double): IVec3<Double> = DoubleImpl(x, x, x)

        @JvmStatic
        @Deprecated("")
        operator fun IVec3<Int>.plus(dir: Direction) = plus(dir.stepX, dir.stepY, dir.stepZ)

        @JvmStatic
        inline fun <reified N : Number> Array<IVec3<N>>.flat(): Array<N> {
            return flatMap { listOf(it.x, it.y, it.z) }.toTypedArray()
        }
    }
}
