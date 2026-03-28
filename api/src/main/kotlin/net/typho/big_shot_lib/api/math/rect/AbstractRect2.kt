package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec2i
import net.typho.big_shot_lib.api.math.vec.NeoVec3i

abstract class AbstractRect2<N : Number>(
    min: AbstractVec2<N>,
    max: AbstractVec2<N>
) {
    @JvmField
    val min: AbstractVec2<N> = min.min(max)
    @JvmField
    val max: AbstractVec2<N> = min.max(max)
    protected abstract val opSet: OperatorSet<N>
    val size: AbstractVec2<N>
        get() = max - min
    val area: N
        get() = opSet.times(size.x, size.y)
    val minMax: AbstractVec2<N> = min.create(min.x, max.y)
    val maxMin: AbstractVec2<N> = min.create(max.x, min.y)

    abstract fun create(min: AbstractVec2<N>, max: AbstractVec2<N>): AbstractRect2<N>

    fun include(other: AbstractRect2<N>): AbstractRect2<N> {
        return create(min.min(other.min), max.max(other.max))
    }

    fun include(other: AbstractVec2<N>): AbstractRect2<N> {
        return create(min.min(other), max.max(other))
    }

    fun contains(other: AbstractRect2<N>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: AbstractVec2<N>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: AbstractRect2<N>): Boolean {
        return min.anyLessThan(other.max) && max.anyGreaterThan(other.min)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractRect2<*>) return false

        if (min != other.min) return false
        if (max != other.max) return false

        return true
    }

    override fun hashCode(): Int {
        var result = min.hashCode()
        result = 21 * result + max.hashCode()
        return result
    }

    companion object {
        @JvmStatic
        val <N : Number> AbstractRect2<N>.sizeInclusive: AbstractVec2<N>
            get() = size + 1

        @JvmStatic
        val AbstractRect2<Int>.areaInclusive: Int
            get() = opSet.times(size.x + 1, size.y + 1)

        @JvmStatic
        operator fun AbstractRect2<Int>.iterator(): Iterator<AbstractVec2<Int>> = (min.x..max.x)
            .flatMap { x ->
                (min.y..max.y).map { y -> NeoVec2i(x, y) }
            }
            .iterator()
    }
}