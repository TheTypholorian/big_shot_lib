package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec2

abstract class AbstractRect2<N : Number, R2 : AbstractRect2<N, R2, V2>, V2 : AbstractVec2<N, V2>>(
    min: V2,
    max: V2
) {
    @JvmField
    val min: V2 = min.min(max)
    @JvmField
    val max: V2 = min.max(max)
    protected abstract val opSet: OperatorSet<N>
    val size: V2
        get() = max - min
    val area: N
        get() = opSet.times(size.x, size.y)

    protected abstract fun create(min: V2, max: V2): R2

    fun include(other: AbstractRect2<N, *, *>): R2 {
        return create(min.min(other.min), max.max(other.max))
    }

    fun include(other: AbstractVec2<N, *>): R2 {
        return create(min.min(other), max.max(other))
    }

    fun contains(other: AbstractRect2<N, *, *>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: AbstractVec2<N, *>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: AbstractRect2<N, *, *>): Boolean {
        return min.anyLessThan(other.max) && max.anyGreaterThan(other.min)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractRect2<*, *, *>) return false

        if (min != other.min) return false
        if (max != other.max) return false

        return true
    }

    override fun hashCode(): Int {
        var result = min.hashCode()
        result = 21 * result + max.hashCode()
        return result
    }
}