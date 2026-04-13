package net.typho.big_shot_lib.api.math.vec

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.util.resource.NeoCodecs
import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector2i

interface IVec2<N : Number> {
    val opSet: OperatorSet<N>

    val x: N
    val y: N

    val gridLength: N
        get() = opSet.plus(opSet.abs(x), opSet.abs(y))
    val lengthSquared: N
        get() = opSet.plus(opSet.times(x, x), opSet.times(y, y))
    val length: Float
        get() = opSet.sqrt(lengthSquared)
    val abs: IVec2<N>
        get() = copyWith(opSet.abs(x), opSet.abs(y))

    fun copyWith(x: N, y: N): IVec2<N>

    fun toInt(): IVec2<Int> = NeoVec2i(x.toInt(), y.toInt())

    fun toFloat(): IVec2<Float> = NeoVec2f(x.toFloat(), y.toFloat())

    fun toDouble(): IVec2<Double> = NeoVec2d(x.toDouble(), y.toDouble())

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

    operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException(index)
    }

    fun equals(x: N, y: N): Boolean {
        return this.x == x && this.y == y
    }

    fun equals(other: IVec2<N>): Boolean {
        return equals(other.x, other.y)
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<IVec2<Int>> = NeoCodecs.createList(2, Codec.INT, { NeoVec2i(it[0], it[1]) }, { listOf(it.x, it.y) })
        @JvmField
        val FLOAT_CODEC: Codec<IVec2<Float>> = NeoCodecs.createList(2, Codec.FLOAT, { NeoVec2f(it[0], it[1]) }, { listOf(it.x, it.y) })
        @JvmField
        val DOUBLE_CODEC: Codec<IVec2<Double>> = NeoCodecs.createList(2, Codec.DOUBLE, { NeoVec2d(it[0], it[1]) }, { listOf(it.x, it.y) })

        @JvmStatic
        fun IVec2<Int>.toJOML() = Vector2i(x, y)

        @JvmStatic
        fun IVec2<Float>.toJOML() = Vector2f(x, y)

        @JvmStatic
        fun IVec2<Double>.toJOML() = Vector2d(x, y)
    }
}
