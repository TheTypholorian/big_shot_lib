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
        return create(opSet.plus(this.x, x), opSet.plus(this.y, y), opSet.plus(this.z, z))
    }

    fun minus(x: N, y: N, z: N): V3 {
        return create(opSet.minus(this.x, x), opSet.minus(this.y, y), opSet.minus(this.z, z))
    }

    fun times(x: N, y: N, z: N): V3 {
        return create(opSet.times(this.x, x), opSet.times(this.y, y), opSet.times(this.z, z))
    }

    fun div(x: N, y: N, z: N): V3 {
        return create(opSet.div(this.x, x), opSet.div(this.y, y), opSet.div(this.z, z))
    }

    fun rem(x: N, y: N, z: N): V3 {
        return create(opSet.rem(this.x, x), opSet.rem(this.y, y), opSet.rem(this.z, z))
    }

    fun min(x: N, y: N, z: N): V3 {
        return create(opSet.min(this.x, x), opSet.min(this.y, y), opSet.min(this.z, z))
    }

    fun max(x: N, y: N, z: N): V3 {
        return create(opSet.max(this.x, x), opSet.max(this.y, y), opSet.max(this.z, z))
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

    operator fun plus(other: AbstractVec3<N, *>) = plus(other.x, other.y, other.z)

    operator fun plus(x: N) = plus(x, x, x)

    fun plus(direction: NeoDirection) = plus(
        opSet.fromInt(direction.inc.x),
        opSet.fromInt(direction.inc.y),
        opSet.fromInt(direction.inc.z)
    )

    fun plus(direction: NeoDirection, x: N) = plus(
        opSet.times(opSet.fromInt(direction.inc.x), x),
        opSet.times(opSet.fromInt(direction.inc.y), x),
        opSet.times(opSet.fromInt(direction.inc.z), x)
    )

    operator fun minus(other: AbstractVec3<N, *>) = minus(other.x, other.y, other.z)

    operator fun minus(x: N) = minus(x, x, x)

    operator fun times(other: AbstractVec3<N, *>) = times(other.x, other.y, other.z)

    operator fun times(x: N) = times(x, x, x)

    operator fun div(other: AbstractVec3<N, *>) = div(other.x, other.y, other.z)

    operator fun div(x: N) = div(x, x, x)

    operator fun rem(other: AbstractVec3<N, *>) = rem(other.x, other.y, other.z)

    operator fun rem(x: N) = rem(x, x, x)

    fun min(other: AbstractVec3<N, *>) = min(other.x, other.y, other.z)

    fun min(x: N) = min(x, x, x)

    fun max(other: AbstractVec3<N, *>) = max(other.x, other.y, other.z)

    fun max(x: N) = max(x, x, x)

    fun distance(other: AbstractVec3<N, *>) = distance(other.x, other.y, other.z)

    fun distanceSquared(other: AbstractVec3<N, *>) = distanceSquared(other.x, other.y, other.z)

    fun gridDistance(other: AbstractVec3<N, *>) = gridDistance(other.x, other.y, other.z)

    fun inDistance(other: AbstractVec3<N, *>, dist: N) = inDistance(other.x, other.y, other.z, dist)

    fun inDistanceSquared(other: AbstractVec3<N, *>, dist: N) = inDistanceSquared(other.x, other.y, other.z, dist)

    fun inGridDistance(other: AbstractVec3<N, *>, dist: N) = inGridDistance(other.x, other.y, other.z, dist)

    fun anyGreaterThan(other: AbstractVec3<N, *>) = anyGreaterThan(other.x, other.y, other.z)

    fun anyGreaterThan(x: N) = anyGreaterThan(x, x, x)

    fun allGreaterThan(other: AbstractVec3<N, *>) = allGreaterThan(other.x, other.y, other.z)

    fun allGreaterThan(x: N) = allGreaterThan(x, x, x)

    fun anyGequalThan(other: AbstractVec3<N, *>) = anyGequalThan(other.x, other.y, other.z)

    fun anyGequalThan(x: N) = anyGequalThan(x, x, x)

    fun allGequalThan(other: AbstractVec3<N, *>) = allGequalThan(other.x, other.y, other.z)

    fun allGequalThan(x: N) = allGequalThan(x, x, x)

    fun anyLessThan(other: AbstractVec3<N, *>) = anyLessThan(other.x, other.y, other.z)

    fun anyLessThan(x: N) = anyLessThan(x, x, x)

    fun allLessThan(other: AbstractVec3<N, *>) = allLessThan(other.x, other.y, other.z)

    fun allLessThan(x: N) = allLessThan(x, x, x)

    fun anyLequalThan(other: AbstractVec3<N, *>) = anyLequalThan(other.x, other.y, other.z)

    fun anyLequalThan(x: N) = anyLequalThan(x, x, x)

    fun allLequalThan(other: AbstractVec3<N, *>) = allLequalThan(other.x, other.y, other.z)

    fun allLequalThan(x: N) = allLequalThan(x, x, x)

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
    }
}
