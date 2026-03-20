package net.typho.big_shot_lib.api.math.vec

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.util.resource.NeoCodecs
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

    fun plus(x: Number, y: Number): V2 {
        return create(opSet.plus(this.x, opSet.convert(x)), opSet.plus(this.y, opSet.convert(y)))
    }

    fun minus(x: Number, y: Number): V2 {
        return create(opSet.minus(this.x, opSet.convert(x)), opSet.minus(this.y, opSet.convert(y)))
    }

    fun times(x: Number, y: Number): V2 {
        return create(opSet.times(this.x, opSet.convert(x)), opSet.times(this.y, opSet.convert(y)))
    }

    fun div(x: Number, y: Number): V2 {
        return create(opSet.div(this.x, opSet.convert(x)), opSet.div(this.y, opSet.convert(y)))
    }

    fun rem(x: Number, y: Number): V2 {
        return create(opSet.rem(this.x, opSet.convert(x)), opSet.rem(this.y, opSet.convert(y)))
    }

    fun min(x: Number, y: Number): V2 {
        return create(opSet.min(this.x, opSet.convert(x)), opSet.min(this.y, opSet.convert(y)))
    }

    fun max(x: Number, y: Number): V2 {
        return create(opSet.max(this.x, opSet.convert(x)), opSet.max(this.y, opSet.convert(y)))
    }

    fun distance(x: Number, y: Number): Float {
        return minus(x, y).length
    }

    fun distanceSquared(x: Number, y: Number): N {
        return minus(x, y).lengthSquared
    }

    fun gridDistance(x: Number, y: Number): N {
        return minus(x, y).gridLength
    }

    fun inDistance(x: Number, y: Number, dist: N): Boolean {
        return inDistanceSquared(x, y, opSet.times(dist, dist))
    }

    fun inDistanceSquared(x: Number, y: Number, dist: N): Boolean {
        return opSet.lessThan(distanceSquared(x, y), dist)
    }

    fun inGridDistance(x: Number, y: Number, dist: N): Boolean {
        return opSet.lessThan(gridDistance(x, y), dist)
    }

    fun minComponent(): N {
        return opSet.min(x, y)
    }

    fun maxComponent(): N {
        return opSet.max(x, y)
    }

    fun anyGreaterThan(x: Number, y: Number): Boolean {
        return opSet.greaterThan(this.x, opSet.convert(x)) || opSet.greaterThan(this.y, opSet.convert(y))
    }

    fun allGreaterThan(x: Number, y: Number): Boolean {
        return opSet.greaterThan(this.x, opSet.convert(x)) && opSet.greaterThan(this.y, opSet.convert(y))
    }

    fun anyGequalThan(x: Number, y: Number): Boolean {
        return opSet.gequalThan(this.x, opSet.convert(x)) || opSet.gequalThan(this.y, opSet.convert(y))
    }

    fun allGequalThan(x: Number, y: Number): Boolean {
        return opSet.gequalThan(this.x, opSet.convert(x)) && opSet.gequalThan(this.y, opSet.convert(y))
    }

    fun anyLessThan(x: Number, y: Number): Boolean {
        return opSet.lessThan(this.x, opSet.convert(x)) || opSet.lessThan(this.y, opSet.convert(y))
    }

    fun allLessThan(x: Number, y: Number): Boolean {
        return opSet.lessThan(this.x, opSet.convert(x)) && opSet.lessThan(this.y, opSet.convert(y))
    }

    fun anyLequalThan(x: Number, y: Number): Boolean {
        return opSet.lequalThan(this.x, opSet.convert(x)) || opSet.lequalThan(this.y, opSet.convert(y))
    }

    fun allLequalThan(x: Number, y: Number): Boolean {
        return opSet.lequalThan(this.x, opSet.convert(x)) && opSet.lequalThan(this.y, opSet.convert(y))
    }

    operator fun plus(other: AbstractVec2<*, *>) = plus(other.x, other.y)

    operator fun plus(x: Number) = plus(x, x)

    operator fun minus(other: AbstractVec2<*, *>) = minus(other.x, other.y)

    operator fun minus(x: Number) = minus(x, x)

    operator fun times(other: AbstractVec2<*, *>) = times(other.x, other.y)

    operator fun times(x: Number) = times(x, x)

    operator fun div(other: AbstractVec2<*, *>) = div(other.x, other.y)

    operator fun div(x: Number) = div(x, x)

    operator fun rem(other: AbstractVec2<*, *>) = rem(other.x, other.y)

    operator fun rem(x: Number) = rem(x, x)

    fun min(other: AbstractVec2<*, *>) = min(other.x, other.y)

    fun min(x: Number) = min(x, x)

    fun max(other: AbstractVec2<*, *>) = max(other.x, other.y)

    fun max(x: Number) = max(x, x)

    fun distance(other: AbstractVec2<*, *>) = distance(other.x, other.y)

    fun distanceSquared(other: AbstractVec2<*, *>) = distanceSquared(other.x, other.y)

    fun gridDistance(other: AbstractVec2<*, *>) = gridDistance(other.x, other.y)

    fun inDistance(other: AbstractVec2<*, *>, dist: N) = inDistance(other.x, other.y, dist)

    fun inDistanceSquared(other: AbstractVec2<*, *>, dist: N) = inDistanceSquared(other.x, other.y, dist)

    fun inGridDistance(other: AbstractVec2<*, *>, dist: N) = inGridDistance(other.x, other.y, dist)

    fun anyGreaterThan(other: AbstractVec2<*, *>) = anyGreaterThan(other.x, other.y)

    fun anyGreaterThan(x: Number) = anyGreaterThan(x, x)

    fun allGreaterThan(other: AbstractVec2<*, *>) = allGreaterThan(other.x, other.y)

    fun allGreaterThan(x: Number) = allGreaterThan(x, x)

    fun anyGequalThan(other: AbstractVec2<*, *>) = anyGequalThan(other.x, other.y)

    fun anyGequalThan(x: Number) = anyGequalThan(x, x)

    fun allGequalThan(other: AbstractVec2<*, *>) = allGequalThan(other.x, other.y)

    fun allGequalThan(x: Number) = allGequalThan(x, x)

    fun anyLessThan(other: AbstractVec2<*, *>) = anyLessThan(other.x, other.y)

    fun anyLessThan(x: Number) = anyLessThan(x, x)

    fun allLessThan(other: AbstractVec2<*, *>) = allLessThan(other.x, other.y)

    fun allLessThan(x: Number) = allLessThan(x, x)

    fun anyLequalThan(other: AbstractVec2<*, *>) = anyLequalThan(other.x, other.y)

    fun anyLequalThan(x: Number) = anyLequalThan(x, x)

    fun allLequalThan(other: AbstractVec2<*, *>) = allLequalThan(other.x, other.y)

    fun allLequalThan(x: Number) = allLequalThan(x, x)

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
        result = 21 * result + y.hashCode()
        return result
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<AbstractVec2<Int, *>> = NeoCodecs.createList(2, Codec.INT, { NeoVec2i(it[0], it[1]) }, { listOf(it.x, it.y) })
        @JvmField
        val FLOAT_CODEC: Codec<AbstractVec2<Float, *>> = NeoCodecs.createList(2, Codec.FLOAT, { NeoVec2f(it[0], it[1]) }, { listOf(it.x, it.y) })
        @JvmField
        val DOUBLE_CODEC: Codec<AbstractVec2<Double, *>> = NeoCodecs.createList(2, Codec.DOUBLE, { NeoVec2d(it[0], it[1]) }, { listOf(it.x, it.y) })

        @JvmStatic
        fun AbstractVec2<Int, *>.toJOML() = Vector2i(x, y)

        @JvmStatic
        fun AbstractVec2<Float, *>.toJOML() = Vector2f(x, y)

        @JvmStatic
        fun AbstractVec2<Double, *>.toJOML() = Vector2d(x, y)

        @JvmStatic
        val AbstractVec2<Int, *>.center: AbstractVec2<Float, *>
            get() = NeoVec2f(0.5f, 0.5f) + this
    }
}
