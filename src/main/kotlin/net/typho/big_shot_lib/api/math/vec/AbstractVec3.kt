package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector3i

abstract class AbstractVec3<N : Number, V3 : AbstractVec3<N, V3>>(
    @JvmField
    val x: N,
    @JvmField
    val y: N,
    @JvmField
    val z: N
) {
    protected abstract val opSet: OperatorSet<N>
    val gridLength: N
        get() = opSet.plus(opSet.abs(x), opSet.plus(opSet.abs(y), opSet.abs(z)))
    val lengthSquared: N
        get() = opSet.plus(opSet.times(x, x), opSet.plus(opSet.times(y, y), opSet.times(z, z)))
    val length: Float
        get() = opSet.sqrt(lengthSquared)
    val abs: V3
        get() = create(opSet.abs(x), opSet.abs(y), opSet.abs(z))

    constructor(other: AbstractVec3<N, *>) : this(other.x, other.y, other.z)

    protected abstract fun create(x: N, y: N, z: N): V3

    fun plus(x: N, y: N, z: N): V3 {
        return create(opSet.plus(x, this.x), opSet.plus(y, this.y), opSet.plus(z, this.z))
    }

    fun minus(x: N, y: N, z: N): V3 {
        return create(opSet.minus(x, this.x), opSet.minus(y, this.y), opSet.minus(z, this.z))
    }

    fun times(x: N, y: N, z: N): V3 {
        return create(opSet.times(x, this.x), opSet.times(y, this.y), opSet.times(z, this.z))
    }

    fun div(x: N, y: N, z: N): V3 {
        return create(opSet.div(x, this.x), opSet.div(y, this.y), opSet.div(z, this.z))
    }

    fun rem(x: N, y: N, z: N): V3 {
        return create(opSet.rem(x, this.x), opSet.rem(y, this.y), opSet.rem(z, this.z))
    }

    fun min(x: N, y: N, z: N): V3 {
        return create(opSet.min(x, this.x), opSet.min(y, this.y), opSet.min(z, this.z))
    }

    fun max(x: N, y: N, z: N): V3 {
        return create(opSet.max(x, this.x), opSet.max(y, this.y), opSet.max(z, this.z))
    }

    fun distance(other: AbstractVec3<N, *>): Float {
        return (this - other).length
    }

    fun distanceSquared(other: AbstractVec3<N, *>): N {
        return (this - other).lengthSquared
    }

    fun gridDistance(other: AbstractVec3<N, *>): N {
        return (this - other).gridLength
    }

    fun inDistance(other: AbstractVec3<N, *>, dist: N): Boolean {
        return inDistanceSquared(other, opSet.times(dist, dist))
    }

    fun inDistanceSquared(other: AbstractVec3<N, *>, dist: N): Boolean {
        return opSet.lessThan(distanceSquared(other), dist)
    }

    fun inGridDistance(other: AbstractVec3<N, *>, dist: N): Boolean {
        return opSet.lessThan(gridDistance(other), dist)
    }

    operator fun plus(other: AbstractVec3<N, *>) = plus(other.x, other.y, other.z)

    operator fun plus(x: N) = plus(x, x, x)

    operator fun minus(other: AbstractVec3<N, *>) = minus(other.x, other.y, other.z)

    operator fun minus(x: N) = minus(x, x, x)

    operator fun times(other: AbstractVec3<N, *>) = times(other.x, other.y, other.z)

    operator fun times(x: N) = times(x, x, x)

    operator fun div(other: AbstractVec3<N, *>) = div(other.x, other.y, other.z)

    operator fun div(x: N) = div(x, x, x)

    operator fun rem(other: AbstractVec3<N, *>) = rem(other.x, other.y, other.z)

    operator fun rem(x: N) = rem(x, x, x)

    fun min(other: AbstractVec3<N, *>) = min(other.x, other.y, other.z)

    fun max(other: AbstractVec3<N, *>) = max(other.x, other.y, other.z)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = create(opSet.negate(x), opSet.negate(y), opSet.negate(z))

    operator fun inc() = plus(opSet.one, opSet.one, opSet.one)

    operator fun dec() = minus(opSet.one, opSet.one, opSet.one)

    operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IndexOutOfBoundsException(index)
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractVec3<*, *>) return false

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

    companion object {
        @JvmStatic
        fun AbstractVec3<Int, *>.toJOML() = Vector3i(x, y, z)

        @JvmStatic
        fun AbstractVec3<Float, *>.toJOML() = Vector3f(x, y, z)

        @JvmStatic
        fun AbstractVec3<Double, *>.toJOML() = Vector3d(x, y, z)

        @JvmStatic
        operator fun AbstractVec3<Int, *>.plus(dir: NeoDirection) = plus(dir.inc)

        @JvmStatic
        fun AbstractVec3<Int, *>.plus(dir: NeoDirection, index: Int) = plus(dir.inc * index)
    }
}
