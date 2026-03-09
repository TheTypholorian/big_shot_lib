package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector4d
import org.joml.Vector4f
import org.joml.Vector4i

abstract class AbstractVec4<N : Number, V4 : AbstractVec4<N, V4>>(
    @JvmField
    val x: N,
    @JvmField
    val y: N,
    @JvmField
    val z: N,
    @JvmField
    val w: N
) {
    protected abstract val opSet: OperatorSet<N>
    val gridLength: N
        get() = opSet.plus(opSet.abs(x), opSet.plus(opSet.abs(y), opSet.plus(opSet.abs(z), opSet.abs(w))))
    val lengthSquared: N
        get() = opSet.plus(opSet.times(x, x), opSet.plus(opSet.times(y, y), opSet.plus(opSet.times(z, z), opSet.times(w, w))))
    val length: Float
        get() = opSet.sqrt(lengthSquared)
    val abs: V4
        get() = create(opSet.abs(x), opSet.abs(y), opSet.abs(z), opSet.abs(w))

    constructor(other: AbstractVec4<N, *>) : this(other.x, other.y, other.z, other.w)

    protected abstract fun create(x: N, y: N, z: N, w: N): V4

    fun plus(x: N, y: N, z: N, w: N): V4 {
        return create(opSet.plus(x, this.x), opSet.plus(y, this.y), opSet.plus(z, this.z), opSet.plus(w, this.w))
    }

    fun minus(x: N, y: N, z: N, w: N): V4 {
        return create(opSet.minus(x, this.x), opSet.minus(y, this.y), opSet.minus(z, this.z), opSet.minus(w, this.w))
    }

    fun times(x: N, y: N, z: N, w: N): V4 {
        return create(opSet.times(x, this.x), opSet.times(y, this.y), opSet.times(z, this.z), opSet.times(w, this.w))
    }

    fun div(x: N, y: N, z: N, w: N): V4 {
        return create(opSet.div(x, this.x), opSet.div(y, this.y), opSet.div(z, this.z), opSet.div(w, this.w))
    }

    fun rem(x: N, y: N, z: N, w: N): V4 {
        return create(opSet.rem(x, this.x), opSet.rem(y, this.y), opSet.rem(z, this.z), opSet.rem(w, this.w))
    }

    fun min(x: N, y: N, z: N, w: N): V4 {
        return create(opSet.min(x, this.x), opSet.min(y, this.y), opSet.min(z, this.z), opSet.min(w, this.w))
    }

    fun max(x: N, y: N, z: N, w: N): V4 {
        return create(opSet.max(x, this.x), opSet.max(y, this.y), opSet.max(z, this.z), opSet.max(w, this.w))
    }

    fun distance(other: AbstractVec4<N, *>): Float {
        return (this - other).length
    }

    fun distanceSquared(other: AbstractVec4<N, *>): N {
        return (this - other).lengthSquared
    }

    fun gridDistance(other: AbstractVec4<N, *>): N {
        return (this - other).gridLength
    }

    fun inDistance(other: AbstractVec4<N, *>, dist: N): Boolean {
        return inDistanceSquared(other, opSet.times(dist, dist))
    }

    fun inDistanceSquared(other: AbstractVec4<N, *>, dist: N): Boolean {
        return opSet.lessThan(distanceSquared(other), dist)
    }

    fun inGridDistance(other: AbstractVec4<N, *>, dist: N): Boolean {
        return opSet.lessThan(gridDistance(other), dist)
    }

    operator fun plus(other: AbstractVec4<N, *>) = plus(other.x, other.y, other.z, other.w)

    operator fun plus(x: N) = plus(x, x, x, x)

    operator fun minus(other: AbstractVec4<N, *>) = minus(other.x, other.y, other.z, other.w)

    operator fun minus(x: N) = minus(x, x, x, x)

    operator fun times(other: AbstractVec4<N, *>) = times(other.x, other.y, other.z, other.w)

    operator fun times(x: N) = times(x, x, x, x)

    operator fun div(other: AbstractVec4<N, *>) = div(other.x, other.y, other.z, other.w)

    operator fun div(x: N) = div(x, x, x, x)

    operator fun rem(other: AbstractVec4<N, *>) = rem(other.x, other.y, other.z, other.w)

    operator fun rem(x: N) = rem(x, x, x, x)

    fun min(other: AbstractVec4<N, *>) = min(other.x, other.y, other.z, other.w)

    fun max(other: AbstractVec4<N, *>) = max(other.x, other.y, other.z, other.w)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = create(opSet.negate(x), opSet.negate(y), opSet.negate(z), opSet.negate(w))

    operator fun inc() = plus(opSet.one, opSet.one, opSet.one, opSet.one)

    operator fun dec() = minus(opSet.one, opSet.one, opSet.one, opSet.one)

    operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        2 -> z
        3 -> w
        else -> throw IndexOutOfBoundsException(index)
    }

    override fun toString(): String {
        return "($x, $y, $z, $w)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractVec4<*, *>) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (w != other.w) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + w.hashCode()
        return result
    }

    companion object {
        @JvmStatic
        fun AbstractVec4<Int, *>.toJOML() = Vector4i(x, y, z, w)

        @JvmStatic
        fun AbstractVec4<Float, *>.toJOML() = Vector4f(x, y, z, w)

        @JvmStatic
        fun AbstractVec4<Double, *>.toJOML() = Vector4d(x, y, z, w)
    }
}
