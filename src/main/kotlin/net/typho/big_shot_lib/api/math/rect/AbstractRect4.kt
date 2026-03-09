package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec4

abstract class AbstractRect4<N : Number, R4 : AbstractRect4<N, R4, V4>, V4 : AbstractVec4<N, V4>>(
    min: V4,
    max: V4
) {
    @JvmField
    val min: V4 = min.min(max)
    @JvmField
    val max: V4 = min.max(max)
    protected abstract val opSet: OperatorSet<N>
    val size: V4
        get() = max - min
    val area: N
        get() = opSet.times(size.x, opSet.times(size.y, opSet.times(size.z, size.w)))

    protected abstract fun create(min: V4, max: V4): R4

    fun include(other: AbstractRect4<N, *, *>): R4 {
        return create(min.min(other.min), max.max(other.max))
    }

    fun include(other: AbstractVec4<N, *>): R4 {
        return create(min.min(other), max.max(other))
    }

    fun contains(other: AbstractRect4<N, *, *>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: AbstractVec4<N, *>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: AbstractRect4<N, *, *>): Boolean {
        return min.anyLessThan(other.max) && max.anyGreaterThan(other.min)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractRect4<*, *, *>) return false

        if (min != other.min) return false
        if (max != other.max) return false

        return true
    }

    override fun hashCode(): Int {
        var result = min.hashCode()
        result = 41 * result + max.hashCode()
        return result
    }
}