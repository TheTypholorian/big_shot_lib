package net.typho.big_shot_lib.api.math.vec

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.util.resource.NeoCodecs
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
    abstract val xy: AbstractVec2<N, *>
    abstract val yz: AbstractVec2<N, *>
    abstract val zw: AbstractVec2<N, *>
    abstract val xyz: AbstractVec3<N, *>

    val r: N
        get() = x
    val g: N
        get() = y
    val b: N
        get() = z
    val a: N
        get() = w

    abstract val rg: AbstractVec2<N, *>
    abstract val gb: AbstractVec2<N, *>
    abstract val ba: AbstractVec2<N, *>
    abstract val rgb: AbstractVec3<N, *>

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

    fun min(x: N) = min(x, x, x, x)

    fun max(other: AbstractVec4<N, *>) = max(other.x, other.y, other.z, other.w)

    fun max(x: N) = max(x, x, x, x)

    fun distance(other: AbstractVec4<N, *>) = distance(other.x, other.y, other.z, other.w)

    fun distanceSquared(other: AbstractVec4<N, *>) = distanceSquared(other.x, other.y, other.z, other.w)

    fun gridDistance(other: AbstractVec4<N, *>) = gridDistance(other.x, other.y, other.z, other.w)

    fun inDistance(other: AbstractVec4<N, *>, dist: N) = inDistance(other.x, other.y, other.z, other.w, dist)

    fun inDistanceSquared(other: AbstractVec4<N, *>, dist: N) = inDistanceSquared(other.x, other.y, other.z, other.w, dist)

    fun inGridDistance(other: AbstractVec4<N, *>, dist: N) = inGridDistance(other.x, other.y, other.z, other.w, dist)

    fun anyGreaterThan(other: AbstractVec4<N, *>) = anyGreaterThan(other.x, other.y, other.z, other.w)

    fun anyGreaterThan(x: N) = anyGreaterThan(x, x, x, x)

    fun allGreaterThan(other: AbstractVec4<N, *>) = allGreaterThan(other.x, other.y, other.z, other.w)

    fun allGreaterThan(x: N) = allGreaterThan(x, x, x, x)

    fun anyGequalThan(other: AbstractVec4<N, *>) = anyGequalThan(other.x, other.y, other.z, other.w)

    fun anyGequalThan(x: N) = anyGequalThan(x, x, x, x)

    fun allGequalThan(other: AbstractVec4<N, *>) = allGequalThan(other.x, other.y, other.z, other.w)

    fun allGequalThan(x: N) = allGequalThan(x, x, x, x)

    fun anyLessThan(other: AbstractVec4<N, *>) = anyLessThan(other.x, other.y, other.z, other.w)

    fun anyLessThan(x: N) = anyLessThan(x, x, x, x)

    fun allLessThan(other: AbstractVec4<N, *>) = allLessThan(other.x, other.y, other.z, other.w)

    fun allLessThan(x: N) = allLessThan(x, x, x, x)

    fun anyLequalThan(other: AbstractVec4<N, *>) = anyLequalThan(other.x, other.y, other.z, other.w)

    fun anyLequalThan(x: N) = anyLequalThan(x, x, x, x)

    fun allLequalThan(other: AbstractVec4<N, *>) = allLequalThan(other.x, other.y, other.z, other.w)

    fun allLequalThan(x: N) = allLequalThan(x, x, x, x)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = create(opSet.negate(x), opSet.negate(y), opSet.negate(z), opSet.negate(w))

    operator fun inc() = plus(opSet.one, opSet.one, opSet.one, opSet.one)

    operator fun dec() = minus(opSet.one, opSet.one, opSet.one, opSet.one)

    operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        2 -> z
        4 -> w
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
        result = 41 * result + y.hashCode()
        result = 41 * result + z.hashCode()
        result = 41 * result + w.hashCode()
        return result
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<AbstractVec4<Int, *>> = NeoCodecs.createList(4, Codec.INT, { NeoVec4i(it[0], it[1], it[2], it[3]) }, { listOf(it.x, it.y, it.z, it.w) })
        @JvmField
        val FLOAT_CODEC: Codec<AbstractVec4<Float, *>> = NeoCodecs.createList(4, Codec.FLOAT, { NeoVec4f(it[0], it[1], it[2], it[3]) }, { listOf(it.x, it.y, it.z, it.w) })
        @JvmField
        val DOUBLE_CODEC: Codec<AbstractVec4<Double, *>> = NeoCodecs.createList(4, Codec.DOUBLE, { NeoVec4d(it[0], it[1], it[2], it[3]) }, { listOf(it.x, it.y, it.z, it.w) })

        @JvmStatic
        fun AbstractVec4<Int, *>.toJOML() = Vector4i(x, y, z, w)

        @JvmStatic
        fun AbstractVec4<Float, *>.toJOML() = Vector4f(x, y, z, w)

        @JvmStatic
        fun AbstractVec4<Double, *>.toJOML() = Vector4d(x, y, z, w)
    }
}
