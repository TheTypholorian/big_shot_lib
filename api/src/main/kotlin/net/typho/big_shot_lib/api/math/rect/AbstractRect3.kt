package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec3

abstract class AbstractRect3<N : Number, R3 : AbstractRect3<N, R3, V3>, V3 : AbstractVec3<N, V3>>(
    min: V3,
    max: V3
) {
    @JvmField
    val min: V3 = min.min(max)
    @JvmField
    val max: V3 = min.max(max)
    protected abstract val opSet: OperatorSet<N>
    val size: V3
        get() = max - min
    val area: N
        get() = opSet.times(size.x, opSet.times(size.y, size.z))

    protected abstract fun create(min: V3, max: V3): R3

    fun include(other: AbstractRect3<N, *, *>): R3 {
        return create(min.min(other.min), max.max(other.max))
    }

    fun include(other: AbstractVec3<N, *>): R3 {
        return create(min.min(other), max.max(other))
    }

    fun contains(other: AbstractRect3<N, *, *>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: AbstractVec3<N, *>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: AbstractRect3<N, *, *>): Boolean {
        return min.anyLessThan(other.max) && max.anyGreaterThan(other.min)
    }

    fun extend(direction: NeoDirection): R3 {
        return if (direction.axisDirection == NeoDirection.AxisDirection.POSITIVE) {
            create(min, max.plus(direction))
        } else {
            create(min.plus(direction), max)
        }
    }

    fun extend(direction: NeoDirection, x: N): R3 {
        return if (direction.axisDirection == NeoDirection.AxisDirection.POSITIVE) {
            create(min, max.plus(direction.inc * x))
        } else {
            create(min.plus(direction.inc * x), max)
        }
    }

    fun move(direction: NeoDirection) = create(min.plus(direction), max.plus(direction))

    fun move(direction: NeoDirection, x: N) = create(min.plus(direction.inc * x), max.plus(direction.inc * x))

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

    companion object {
        @JvmStatic
        val <V3 : AbstractVec3<Int, V3>> AbstractRect3<Int, *, V3>.sizeInclusive: V3
            get() = size + 1

        @JvmStatic
        val AbstractRect3<Int, *, *>.areaInclusive: Int
            get() = opSet.times(size.x + 1, opSet.times(size.y + 1, size.z + 1))
    }
}