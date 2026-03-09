package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector2i

abstract class AbstractVec2<N : Number, V2 : AbstractVec2<N, V2>>(
    @JvmField
    val x: N,
    @JvmField
    val y: N
) {
    protected abstract val opSet: OperatorSet<N>
    val gridLength: N
        get() = opSet.plus(opSet.abs(x), opSet.abs(y))
    val lengthSquared: N
        get() = opSet.plus(opSet.times(x, x), opSet.times(y, y))
    val length: Float
        get() = opSet.sqrt(lengthSquared)
    val abs: V2
        get() = create(opSet.abs(x), opSet.abs(y))

    constructor(other: AbstractVec2<N, *>) : this(other.x, other.y)

    protected abstract fun create(x: N, y: N): V2

    fun plus(x: N, y: N): V2 {
        return create(opSet.plus(x, this.x), opSet.plus(y, this.y))
    }

    fun minus(x: N, y: N): V2 {
        return create(opSet.minus(x, this.x), opSet.minus(y, this.y))
    }

    fun times(x: N, y: N): V2 {
        return create(opSet.times(x, this.x), opSet.times(y, this.y))
    }

    fun div(x: N, y: N): V2 {
        return create(opSet.div(x, this.x), opSet.div(y, this.y))
    }

    fun rem(x: N, y: N): V2 {
        return create(opSet.rem(x, this.x), opSet.rem(y, this.y))
    }

    fun min(x: N, y: N): V2 {
        return create(opSet.min(x, this.x), opSet.min(y, this.y))
    }

    fun max(x: N, y: N): V2 {
        return create(opSet.max(x, this.x), opSet.max(y, this.y))
    }

    fun distance(other: AbstractVec2<N, *>): Float {
        return (this - other).length
    }

    fun distanceSquared(other: AbstractVec2<N, *>): N {
        return (this - other).lengthSquared
    }

    fun gridDistance(other: AbstractVec2<N, *>): N {
        return (this - other).gridLength
    }

    fun inDistance(other: AbstractVec2<N, *>, dist: N): Boolean {
        return inDistanceSquared(other, opSet.times(dist, dist))
    }

    fun inDistanceSquared(other: AbstractVec2<N, *>, dist: N): Boolean {
        return opSet.lessThan(distanceSquared(other), dist)
    }

    fun inGridDistance(other: AbstractVec2<N, *>, dist: N): Boolean {
        return opSet.lessThan(gridDistance(other), dist)
    }

    operator fun plus(other: AbstractVec2<N, *>) = plus(other.x, other.y)

    operator fun plus(x: N) = plus(x, x)

    operator fun minus(other: AbstractVec2<N, *>) = minus(other.x, other.y)

    operator fun minus(x: N) = minus(x, x)

    operator fun times(other: AbstractVec2<N, *>) = times(other.x, other.y)

    operator fun times(x: N) = times(x, x)

    operator fun div(other: AbstractVec2<N, *>) = div(other.x, other.y)

    operator fun div(x: N) = div(x, x)

    operator fun rem(other: AbstractVec2<N, *>) = rem(other.x, other.y)

    operator fun rem(x: N) = rem(x, x)

    fun min(other: AbstractVec2<N, *>) = min(other.x, other.y)

    fun max(other: AbstractVec2<N, *>) = max(other.x, other.y)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = create(opSet.negate(x), opSet.negate(y))

    operator fun inc() = plus(opSet.one, opSet.one)

    operator fun dec() = minus(opSet.one, opSet.one)

    operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException(index)
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractVec2<*, *>) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    companion object {
        @JvmStatic
        fun AbstractVec2<Int, *>.toJOML() = Vector2i(x, y)

        @JvmStatic
        fun AbstractVec2<Float, *>.toJOML() = Vector2f(x, y)

        @JvmStatic
        fun AbstractVec2<Double, *>.toJOML() = Vector2d(x, y)
    }
}
