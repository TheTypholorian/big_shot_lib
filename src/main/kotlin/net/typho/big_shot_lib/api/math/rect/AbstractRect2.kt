package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec2

abstract class AbstractRect2<N : Number, R2 : AbstractRect2<N, R2, V2>, V2 : AbstractVec2<N, V2>>(
    @JvmField
    val min: V2,
    @JvmField
    val max: V2
) {
    protected abstract val opSet: OperatorSet<N>
    val size: V2
        get() = max - min

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractRect2<*, *, *>) return false

        if (min != other.min) return false
        if (max != other.max) return false

        return true
    }

    override fun hashCode(): Int {
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        return result
    }
}