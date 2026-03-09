package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec3

abstract class AbstractRect3<N : Number, R3 : AbstractRect3<N, R3, V3>, V3 : AbstractVec3<N, V3>>(
    @JvmField
    val min: V3,
    @JvmField
    val max: V3
) {
    protected abstract val opSet: OperatorSet<N>
    val size: V3
        get() = max - min

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractRect3<*, *, *>) return false

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