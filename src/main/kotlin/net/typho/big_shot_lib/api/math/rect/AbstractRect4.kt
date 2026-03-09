package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec4

abstract class AbstractRect4<N : Number, R4 : AbstractRect4<N, R4, V4>, V4 : AbstractVec4<N, V4>>(
    @JvmField
    val min: V4,
    @JvmField
    val max: V4
) {
    protected abstract val opSet: OperatorSet<N>
    val size: V4
        get() = max - min

    protected abstract fun create(x: N, y: N, z: N, w: N): V4

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractRect4<*, *, *>) return false

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