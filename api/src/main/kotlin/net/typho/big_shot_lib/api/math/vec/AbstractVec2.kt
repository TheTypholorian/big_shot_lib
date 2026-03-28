package net.typho.big_shot_lib.api.math.vec

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.util.resource.NeoCodecs
import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector2i

abstract class AbstractVec2<N : Number>(
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
    val abs: AbstractVec2<N>
        get() = create(opSet.abs(x), opSet.abs(y))

    constructor(other: AbstractVec2<N>) : this(other.x, other.y)

    abstract fun create(x: N, y: N): AbstractVec2<N>

    fun toInt(): AbstractVec2<Int> = NeoVec2i(x.toInt(), y.toInt())

    fun toFloat(): AbstractVec2<Float> = NeoVec2f(x.toFloat(), y.toFloat())

    fun toDouble(): AbstractVec2<Double> = NeoVec2d(x.toDouble(), y.toDouble())

    fun lerp(x: N, y: N, d: Float): AbstractVec2<N> {
        return create(opSet.lerp(this.x, x, d), opSet.lerp(this.y, y, d))
    }

    fun plus(x: N, y: N): AbstractVec2<N> {
        return create(opSet.plus(this.x, x), opSet.plus(this.y, y))
    }

    fun minus(x: N, y: N): AbstractVec2<N> {
        return create(opSet.minus(this.x, x), opSet.minus(this.y, y))
    }

    fun times(x: N, y: N): AbstractVec2<N> {
        return create(opSet.times(this.x, x), opSet.times(this.y, y))
    }

    fun div(x: N, y: N): AbstractVec2<N> {
        return create(opSet.div(this.x, x), opSet.div(this.y, y))
    }

    fun rem(x: N, y: N): AbstractVec2<N> {
        return create(opSet.rem(this.x, x), opSet.rem(this.y, y))
    }

    fun min(x: N, y: N): AbstractVec2<N> {
        return create(opSet.min(this.x, x), opSet.min(this.y, y))
    }

    fun max(x: N, y: N): AbstractVec2<N> {
        return create(opSet.max(this.x, x), opSet.max(this.y, y))
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

    fun lerp(other: AbstractVec2<N>, d: Float) = lerp(other.x, other.y, d)

    fun lerp(x: N, d: Float) = lerp(x, x, d)

    operator fun plus(other: AbstractVec2<N>) = plus(other.x, other.y)

    operator fun plus(x: N) = plus(x, x)

    operator fun minus(other: AbstractVec2<N>) = minus(other.x, other.y)

    operator fun minus(x: N) = minus(x, x)

    operator fun times(other: AbstractVec2<N>) = times(other.x, other.y)

    operator fun times(x: N) = times(x, x)

    operator fun div(other: AbstractVec2<N>) = div(other.x, other.y)

    operator fun div(x: N) = div(x, x)

    operator fun rem(other: AbstractVec2<N>) = rem(other.x, other.y)

    operator fun rem(x: N) = rem(x, x)

    fun min(other: AbstractVec2<N>) = min(other.x, other.y)

    fun min(x: N) = min(x, x)

    fun max(other: AbstractVec2<N>) = max(other.x, other.y)

    fun max(x: N) = max(x, x)

    fun distance(other: AbstractVec2<N>) = distance(other.x, other.y)

    fun distanceSquared(other: AbstractVec2<N>) = distanceSquared(other.x, other.y)

    fun gridDistance(other: AbstractVec2<N>) = gridDistance(other.x, other.y)

    fun inDistance(other: AbstractVec2<N>, dist: N) = inDistance(other.x, other.y, dist)

    fun inDistanceSquared(other: AbstractVec2<N>, dist: N) = inDistanceSquared(other.x, other.y, dist)

    fun inGridDistance(other: AbstractVec2<N>, dist: N) = inGridDistance(other.x, other.y, dist)

    fun anyGreaterThan(other: AbstractVec2<N>) = anyGreaterThan(other.x, other.y)

    fun anyGreaterThan(x: N) = anyGreaterThan(x, x)

    fun allGreaterThan(other: AbstractVec2<N>) = allGreaterThan(other.x, other.y)

    fun allGreaterThan(x: N) = allGreaterThan(x, x)

    fun anyGequalThan(other: AbstractVec2<N>) = anyGequalThan(other.x, other.y)

    fun anyGequalThan(x: N) = anyGequalThan(x, x)

    fun allGequalThan(other: AbstractVec2<N>) = allGequalThan(other.x, other.y)

    fun allGequalThan(x: N) = allGequalThan(x, x)

    fun anyLessThan(other: AbstractVec2<N>) = anyLessThan(other.x, other.y)

    fun anyLessThan(x: N) = anyLessThan(x, x)

    fun allLessThan(other: AbstractVec2<N>) = allLessThan(other.x, other.y)

    fun allLessThan(x: N) = allLessThan(x, x)

    fun anyLequalThan(other: AbstractVec2<N>) = anyLequalThan(other.x, other.y)

    fun anyLequalThan(x: N) = anyLequalThan(x, x)

    fun allLequalThan(other: AbstractVec2<N>) = allLequalThan(other.x, other.y)

    fun allLequalThan(x: N) = allLequalThan(x, x)

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
        if (other !is AbstractVec2<N>) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    fun equals(x: N, y: N): Boolean {
        return this.x == x && this.y == y
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 21 * result + y.hashCode()
        return result
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<AbstractVec2<Int>> = NeoCodecs.createList(2, Codec.INT, { NeoVec2i(it[0], it[1]) }, { listOf(it.x, it.y) })
        @JvmField
        val FLOAT_CODEC: Codec<AbstractVec2<Float>> = NeoCodecs.createList(2, Codec.FLOAT, { NeoVec2f(it[0], it[1]) }, { listOf(it.x, it.y) })
        @JvmField
        val DOUBLE_CODEC: Codec<AbstractVec2<Double>> = NeoCodecs.createList(2, Codec.DOUBLE, { NeoVec2d(it[0], it[1]) }, { listOf(it.x, it.y) })

        @JvmStatic
        fun AbstractVec2<Int>.toJOML() = Vector2i(x, y)

        @JvmStatic
        fun AbstractVec2<Float>.toJOML() = Vector2f(x, y)

        @JvmStatic
        fun AbstractVec2<Double>.toJOML() = Vector2d(x, y)

        @JvmStatic
        val AbstractVec2<Int>.center: AbstractVec2<Float>
            get() = toFloat() + NeoVec2f(0.5f, 0.5f)
    }
}
