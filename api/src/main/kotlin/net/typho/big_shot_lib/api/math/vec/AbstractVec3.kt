package net.typho.big_shot_lib.api.math.vec

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.util.resource.NeoCodecs
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
    abstract val xy: AbstractVec2<N, *>
    abstract val yz: AbstractVec2<N, *>

    val r: N
        get() = x
    val g: N
        get() = y
    val b: N
        get() = z

    abstract val rg: AbstractVec2<N, *>
    abstract val gb: AbstractVec2<N, *>

    constructor(other: AbstractVec3<N, *>) : this(other.x, other.y, other.z)

    protected abstract fun create(x: N, y: N, z: N): V3

    fun plus(x: Number, y: Number, z: Number): V3 {
        return create(opSet.plus(this.x, opSet.convert(x)), opSet.plus(this.y, opSet.convert(y)), opSet.plus(this.z, opSet.convert(z)))
    }

    fun minus(x: Number, y: Number, z: Number): V3 {
        return create(opSet.minus(this.x, opSet.convert(x)), opSet.minus(this.y, opSet.convert(y)), opSet.minus(this.z, opSet.convert(z)))
    }

    fun times(x: Number, y: Number, z: Number): V3 {
        return create(opSet.times(this.x, opSet.convert(x)), opSet.times(this.y, opSet.convert(y)), opSet.times(this.z, opSet.convert(z)))
    }

    fun div(x: Number, y: Number, z: Number): V3 {
        return create(opSet.div(this.x, opSet.convert(x)), opSet.div(this.y, opSet.convert(y)), opSet.div(this.z, opSet.convert(z)))
    }

    fun rem(x: Number, y: Number, z: Number): V3 {
        return create(opSet.rem(this.x, opSet.convert(x)), opSet.rem(this.y, opSet.convert(y)), opSet.rem(this.z, opSet.convert(z)))
    }

    fun min(x: Number, y: Number, z: Number): V3 {
        return create(opSet.min(this.x, opSet.convert(x)), opSet.min(this.y, opSet.convert(y)), opSet.min(this.z, opSet.convert(z)))
    }

    fun max(x: Number, y: Number, z: Number): V3 {
        return create(opSet.max(this.x, opSet.convert(x)), opSet.max(this.y, opSet.convert(y)), opSet.max(this.z, opSet.convert(z)))
    }

    fun distance(x: Number, y: Number, z: Number): Float {
        return minus(x, y, z).length
    }

    fun distanceSquared(x: Number, y: Number, z: Number): N {
        return minus(x, y, z).lengthSquared
    }

    fun gridDistance(x: Number, y: Number, z: Number): N {
        return minus(x, y, z).gridLength
    }

    fun inDistance(x: Number, y: Number, z: Number, dist: Number): Boolean {
        return inDistanceSquared(x, y, z, opSet.times(opSet.convert(dist), opSet.convert(dist)))
    }

    fun inDistanceSquared(x: Number, y: Number, z: Number, dist: Number): Boolean {
        return opSet.lessThan(distanceSquared(x, y, z), opSet.convert(dist))
    }

    fun inGridDistance(x: Number, y: Number, z: Number, dist: Number): Boolean {
        return opSet.lessThan(gridDistance(x, y, z), opSet.convert(dist))
    }

    fun minComponent(): N {
        return opSet.min(x, opSet.min(y, z))
    }

    fun maxComponent(): N {
        return opSet.max(x, opSet.max(y, z))
    }

    fun anyGreaterThan(x: Number, y: Number, z: Number): Boolean {
        return opSet.greaterThan(this.x, opSet.convert(x)) || opSet.greaterThan(this.y, opSet.convert(y)) || opSet.greaterThan(this.z, opSet.convert(z))
    }

    fun allGreaterThan(x: Number, y: Number, z: Number): Boolean {
        return opSet.greaterThan(this.x, opSet.convert(x)) && opSet.greaterThan(this.y, opSet.convert(y)) && opSet.greaterThan(this.z, opSet.convert(z))
    }

    fun anyGequalThan(x: Number, y: Number, z: Number): Boolean {
        return opSet.gequalThan(this.x, opSet.convert(x)) || opSet.gequalThan(this.y, opSet.convert(y)) || opSet.gequalThan(this.z, opSet.convert(z))
    }

    fun allGequalThan(x: Number, y: Number, z: Number): Boolean {
        return opSet.gequalThan(this.x, opSet.convert(x)) && opSet.gequalThan(this.y, opSet.convert(y)) && opSet.gequalThan(this.z, opSet.convert(z))
    }

    fun anyLessThan(x: Number, y: Number, z: Number): Boolean {
        return opSet.lessThan(this.x, opSet.convert(x)) || opSet.lessThan(this.y, opSet.convert(y)) || opSet.lessThan(this.z, opSet.convert(z))
    }

    fun allLessThan(x: Number, y: Number, z: Number): Boolean {
        return opSet.lessThan(this.x, opSet.convert(x)) && opSet.lessThan(this.y, opSet.convert(y)) && opSet.lessThan(this.z, opSet.convert(z))
    }

    fun anyLequalThan(x: Number, y: Number, z: Number): Boolean {
        return opSet.lequalThan(this.x, opSet.convert(x)) || opSet.lequalThan(this.y, opSet.convert(y)) || opSet.lequalThan(this.z, opSet.convert(z))
    }

    fun allLequalThan(x: Number, y: Number, z: Number): Boolean {
        return opSet.lequalThan(this.x, opSet.convert(x)) && opSet.lequalThan(this.y, opSet.convert(y)) && opSet.lequalThan(this.z, opSet.convert(z))
    }

    operator fun plus(other: AbstractVec3<*, *>) = plus(other.x, other.y, other.z)

    operator fun plus(x: Number) = plus(x, x, x)

    operator fun plus(direction: NeoDirection) = plus(direction.inc)

    operator fun minus(other: AbstractVec3<*, *>) = minus(other.x, other.y, other.z)

    operator fun minus(x: Number) = minus(x, x, x)

    operator fun times(other: AbstractVec3<*, *>) = times(other.x, other.y, other.z)

    operator fun times(x: Number) = times(x, x, x)

    operator fun div(other: AbstractVec3<*, *>) = div(other.x, other.y, other.z)

    operator fun div(x: Number) = div(x, x, x)

    operator fun rem(other: AbstractVec3<*, *>) = rem(other.x, other.y, other.z)

    operator fun rem(x: Number) = rem(x, x, x)

    fun min(other: AbstractVec3<*, *>) = min(other.x, other.y, other.z)

    fun min(x: Number) = min(x, x, x)

    fun max(other: AbstractVec3<*, *>) = max(other.x, other.y, other.z)

    fun max(x: Number) = max(x, x, x)

    fun distance(other: AbstractVec3<*, *>) = distance(other.x, other.y, other.z)

    fun distanceSquared(other: AbstractVec3<*, *>) = distanceSquared(other.x, other.y, other.z)

    fun gridDistance(other: AbstractVec3<*, *>) = gridDistance(other.x, other.y, other.z)

    fun inDistance(other: AbstractVec3<*, *>, dist: N) = inDistance(other.x, other.y, other.z, dist)

    fun inDistanceSquared(other: AbstractVec3<*, *>, dist: N) = inDistanceSquared(other.x, other.y, other.z, dist)

    fun inGridDistance(other: AbstractVec3<*, *>, dist: N) = inGridDistance(other.x, other.y, other.z, dist)

    fun anyGreaterThan(other: AbstractVec3<*, *>) = anyGreaterThan(other.x, other.y, other.z)

    fun anyGreaterThan(x: Number) = anyGreaterThan(x, x, x)

    fun allGreaterThan(other: AbstractVec3<*, *>) = allGreaterThan(other.x, other.y, other.z)

    fun allGreaterThan(x: Number) = allGreaterThan(x, x, x)

    fun anyGequalThan(other: AbstractVec3<*, *>) = anyGequalThan(other.x, other.y, other.z)

    fun anyGequalThan(x: Number) = anyGequalThan(x, x, x)

    fun allGequalThan(other: AbstractVec3<*, *>) = allGequalThan(other.x, other.y, other.z)

    fun allGequalThan(x: Number) = allGequalThan(x, x, x)

    fun anyLessThan(other: AbstractVec3<*, *>) = anyLessThan(other.x, other.y, other.z)

    fun anyLessThan(x: Number) = anyLessThan(x, x, x)

    fun allLessThan(other: AbstractVec3<*, *>) = allLessThan(other.x, other.y, other.z)

    fun allLessThan(x: Number) = allLessThan(x, x, x)

    fun anyLequalThan(other: AbstractVec3<*, *>) = anyLequalThan(other.x, other.y, other.z)

    fun anyLequalThan(x: Number) = anyLequalThan(x, x, x)

    fun allLequalThan(other: AbstractVec3<*, *>) = allLequalThan(other.x, other.y, other.z)

    fun allLequalThan(x: Number) = allLequalThan(x, x, x)

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
        @JvmField
        val INT_CODEC: Codec<AbstractVec3<Int, *>> = NeoCodecs.createList(3, Codec.INT, { NeoVec3i(it[0], it[1], it[2]) }, { listOf(it.x, it.y, it.z) })
        @JvmField
        val FLOAT_CODEC: Codec<AbstractVec3<Float, *>> = NeoCodecs.createList(3, Codec.FLOAT, { NeoVec3f(it[0], it[1], it[2]) }, { listOf(it.x, it.y, it.z) })
        @JvmField
        val DOUBLE_CODEC: Codec<AbstractVec3<Double, *>> = NeoCodecs.createList(3, Codec.DOUBLE, { NeoVec3d(it[0], it[1], it[2]) }, { listOf(it.x, it.y, it.z) })

        @JvmStatic
        fun AbstractVec3<Int, *>.toJOML() = Vector3i(x, y, z)

        @JvmStatic
        fun AbstractVec3<Float, *>.toJOML() = Vector3f(x, y, z)

        @JvmStatic
        fun AbstractVec3<Double, *>.toJOML() = Vector3d(x, y, z)
    }
}
