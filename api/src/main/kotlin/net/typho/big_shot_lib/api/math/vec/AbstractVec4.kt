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

    fun plus(x: Number, y: Number, z: Number, w: Number): V4 {
        return create(opSet.plus(this.x, opSet.convert(x)), opSet.plus(this.y, opSet.convert(y)), opSet.plus(this.z, opSet.convert(z)), opSet.plus(this.w, opSet.convert(w)))
    }

    fun minus(x: Number, y: Number, z: Number, w: Number): V4 {
        return create(opSet.minus(this.x, opSet.convert(x)), opSet.minus(this.y, opSet.convert(y)), opSet.minus(this.z, opSet.convert(z)), opSet.minus(this.w, opSet.convert(w)))
    }

    fun times(x: Number, y: Number, z: Number, w: Number): V4 {
        return create(opSet.times(this.x, opSet.convert(x)), opSet.times(this.y, opSet.convert(y)), opSet.times(this.z, opSet.convert(z)), opSet.times(this.w, opSet.convert(w)))
    }

    fun div(x: Number, y: Number, z: Number, w: Number): V4 {
        return create(opSet.div(this.x, opSet.convert(x)), opSet.div(this.y, opSet.convert(y)), opSet.div(this.z, opSet.convert(z)), opSet.div(this.w, opSet.convert(w)))
    }

    fun rem(x: Number, y: Number, z: Number, w: Number): V4 {
        return create(opSet.rem(this.x, opSet.convert(x)), opSet.rem(this.y, opSet.convert(y)), opSet.rem(this.z, opSet.convert(z)), opSet.rem(this.w, opSet.convert(w)))
    }

    fun min(x: Number, y: Number, z: Number, w: Number): V4 {
        return create(opSet.min(this.x, opSet.convert(x)), opSet.min(this.y, opSet.convert(y)), opSet.min(this.z, opSet.convert(z)), opSet.min(this.w, opSet.convert(w)))
    }

    fun max(x: Number, y: Number, z: Number, w: Number): V4 {
        return create(opSet.max(this.x, opSet.convert(x)), opSet.max(this.y, opSet.convert(y)), opSet.max(this.z, opSet.convert(z)), opSet.max(this.w, opSet.convert(w)))
    }

    fun distance(x: Number, y: Number, z: Number, w: Number): Float {
        return minus(x, y, z, w).length
    }

    fun distanceSquared(x: Number, y: Number, z: Number, w: Number): N {
        return minus(x, y, z, w).lengthSquared
    }

    fun gridDistance(x: Number, y: Number, z: Number, w: Number): N {
        return minus(x, y, z, w).gridLength
    }

    fun inDistance(x: Number, y: Number, z: Number, w: Number, dist: N): Boolean {
        return inDistanceSquared(x, y, z, w, opSet.times(dist, dist))
    }

    fun inDistanceSquared(x: Number, y: Number, z: Number, w: Number, dist: N): Boolean {
        return opSet.lessThan(distanceSquared(x, y, z, w), dist)
    }

    fun inGridDistance(x: Number, y: Number, z: Number, w: Number, dist: N): Boolean {
        return opSet.lessThan(gridDistance(x, y, z, w), dist)
    }

    fun minComponent(): N {
        return opSet.min(x, opSet.min(y, opSet.min(z, w)))
    }

    fun maxComponent(): N {
        return opSet.max(x, opSet.max(y, opSet.max(z, w)))
    }

    fun anyGreaterThan(x: Number, y: Number, z: Number, w: Number): Boolean {
        return opSet.greaterThan(this.x, opSet.convert(x)) || opSet.greaterThan(this.y, opSet.convert(y)) || opSet.greaterThan(this.z, opSet.convert(z)) || opSet.greaterThan(this.w, opSet.convert(w))
    }

    fun allGreaterThan(x: Number, y: Number, z: Number, w: Number): Boolean {
        return opSet.greaterThan(this.x, opSet.convert(x)) && opSet.greaterThan(this.y, opSet.convert(y)) && opSet.greaterThan(this.z, opSet.convert(z)) && opSet.greaterThan(this.w, opSet.convert(w))
    }

    fun anyGequalThan(x: Number, y: Number, z: Number, w: Number): Boolean {
        return opSet.gequalThan(this.x, opSet.convert(x)) || opSet.gequalThan(this.y, opSet.convert(y)) || opSet.gequalThan(this.z, opSet.convert(z)) || opSet.gequalThan(this.w, opSet.convert(w))
    }

    fun allGequalThan(x: Number, y: Number, z: Number, w: Number): Boolean {
        return opSet.gequalThan(this.x, opSet.convert(x)) && opSet.gequalThan(this.y, opSet.convert(y)) && opSet.gequalThan(this.z, opSet.convert(z)) && opSet.gequalThan(this.w, opSet.convert(w))
    }

    fun anyLessThan(x: Number, y: Number, z: Number, w: Number): Boolean {
        return opSet.lessThan(this.x, opSet.convert(x)) || opSet.lessThan(this.y, opSet.convert(y)) || opSet.lessThan(this.z, opSet.convert(z)) || opSet.lessThan(this.w, opSet.convert(w))
    }

    fun allLessThan(x: Number, y: Number, z: Number, w: Number): Boolean {
        return opSet.lessThan(this.x, opSet.convert(x)) && opSet.lessThan(this.y, opSet.convert(y)) && opSet.lessThan(this.z, opSet.convert(z)) && opSet.lessThan(this.w, opSet.convert(w))
    }

    fun anyLequalThan(x: Number, y: Number, z: Number, w: Number): Boolean {
        return opSet.lequalThan(this.x, opSet.convert(x)) || opSet.lequalThan(this.y, opSet.convert(y)) || opSet.lequalThan(this.z, opSet.convert(z)) || opSet.lequalThan(this.w, opSet.convert(w))
    }

    fun allLequalThan(x: Number, y: Number, z: Number, w: Number): Boolean {
        return opSet.lequalThan(this.x, opSet.convert(x)) && opSet.lequalThan(this.y, opSet.convert(y)) && opSet.lequalThan(this.z, opSet.convert(z)) && opSet.lequalThan(this.w, opSet.convert(w))
    }

    operator fun plus(other: AbstractVec4<*, *>) = plus(other.x, other.y, other.z, other.w)

    operator fun plus(x: Number) = plus(x, x, x, x)

    operator fun minus(other: AbstractVec4<*, *>) = minus(other.x, other.y, other.z, other.w)

    operator fun minus(x: Number) = minus(x, x, x, x)

    operator fun times(other: AbstractVec4<*, *>) = times(other.x, other.y, other.z, other.w)

    operator fun times(x: Number) = times(x, x, x, x)

    operator fun div(other: AbstractVec4<*, *>) = div(other.x, other.y, other.z, other.w)

    operator fun div(x: Number) = div(x, x, x, x)

    operator fun rem(other: AbstractVec4<*, *>) = rem(other.x, other.y, other.z, other.w)

    operator fun rem(x: Number) = rem(x, x, x, x)

    fun min(other: AbstractVec4<*, *>) = min(other.x, other.y, other.z, other.w)

    fun min(x: Number) = min(x, x, x, x)

    fun max(other: AbstractVec4<*, *>) = max(other.x, other.y, other.z, other.w)

    fun max(x: Number) = max(x, x, x, x)

    fun distance(other: AbstractVec4<*, *>) = distance(other.x, other.y, other.z, other.w)

    fun distanceSquared(other: AbstractVec4<*, *>) = distanceSquared(other.x, other.y, other.z, other.w)

    fun gridDistance(other: AbstractVec4<*, *>) = gridDistance(other.x, other.y, other.z, other.w)

    fun inDistance(other: AbstractVec4<*, *>, dist: N) = inDistance(other.x, other.y, other.z, other.w, dist)

    fun inDistanceSquared(other: AbstractVec4<*, *>, dist: N) = inDistanceSquared(other.x, other.y, other.z, other.w, dist)

    fun inGridDistance(other: AbstractVec4<*, *>, dist: N) = inGridDistance(other.x, other.y, other.z, other.w, dist)

    fun anyGreaterThan(other: AbstractVec4<*, *>) = anyGreaterThan(other.x, other.y, other.z, other.w)

    fun anyGreaterThan(x: Number) = anyGreaterThan(x, x, x, x)

    fun allGreaterThan(other: AbstractVec4<*, *>) = allGreaterThan(other.x, other.y, other.z, other.w)

    fun allGreaterThan(x: Number) = allGreaterThan(x, x, x, x)

    fun anyGequalThan(other: AbstractVec4<*, *>) = anyGequalThan(other.x, other.y, other.z, other.w)

    fun anyGequalThan(x: Number) = anyGequalThan(x, x, x, x)

    fun allGequalThan(other: AbstractVec4<*, *>) = allGequalThan(other.x, other.y, other.z, other.w)

    fun allGequalThan(x: Number) = allGequalThan(x, x, x, x)

    fun anyLessThan(other: AbstractVec4<*, *>) = anyLessThan(other.x, other.y, other.z, other.w)

    fun anyLessThan(x: Number) = anyLessThan(x, x, x, x)

    fun allLessThan(other: AbstractVec4<*, *>) = allLessThan(other.x, other.y, other.z, other.w)

    fun allLessThan(x: Number) = allLessThan(x, x, x, x)

    fun anyLequalThan(other: AbstractVec4<*, *>) = anyLequalThan(other.x, other.y, other.z, other.w)

    fun anyLequalThan(x: Number) = anyLequalThan(x, x, x, x)

    fun allLequalThan(other: AbstractVec4<*, *>) = allLequalThan(other.x, other.y, other.z, other.w)

    fun allLequalThan(x: Number) = allLequalThan(x, x, x, x)

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

        @JvmStatic
        val AbstractVec4<Int, *>.center: AbstractVec4<Float, *>
            get() = NeoVec4f(0.5f, 0.5f, 0.5f, 0.5f) + this
    }
}
