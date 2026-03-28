package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.rect.AbstractRect3.Companion.iterator
import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3i

abstract class AbstractRect3<N : Number>(
    min: AbstractVec3<N>,
    max: AbstractVec3<N>
) {
    @JvmField
    val min: AbstractVec3<N> = min.min(max)
    @JvmField
    val max: AbstractVec3<N> = min.max(max)
    protected abstract val opSet: OperatorSet<N>
    val size: AbstractVec3<N>
        get() = max - min
    val area: N
        get() = opSet.times(size.x, opSet.times(size.y, size.z))

    abstract fun create(min: AbstractVec3<N>, max: AbstractVec3<N>): AbstractRect3<N>

    fun include(other: AbstractRect3<N>): AbstractRect3<N> {
        return create(min.min(other.min), max.max(other.max))
    }

    fun include(other: AbstractVec3<N>): AbstractRect3<N> {
        return create(min.min(other), max.max(other))
    }

    fun contains(other: AbstractRect3<N>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: AbstractVec3<N>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: AbstractRect3<N>): Boolean {
        return min.anyLessThan(other.max) && max.anyGreaterThan(other.min)
    }

    fun extend(direction: NeoDirection): AbstractRect3<N> {
        return if (direction.axisDirection == NeoDirection.AxisDirection.POSITIVE) {
            create(min, max.plus(direction))
        } else {
            create(min.plus(direction), max)
        }
    }

    fun extend(direction: NeoDirection, x: N): AbstractRect3<N> {
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
        if (other !is AbstractRect3<*>) return false

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
        val <N : Number> AbstractRect3<N>.sizeInclusive: AbstractVec3<N>
            get() = size + 1

        @JvmStatic
        val AbstractRect3<Int>.areaInclusive: Int
            get() = opSet.times(size.x + 1, opSet.times(size.y + 1, size.z + 1))

        @JvmStatic
        operator fun AbstractRect3<Int>.iterator(): Iterator<AbstractVec3<Int>> = (min.x..max.x)
            .flatMap { x ->
                (min.y..max.y).map { y -> x to y }
            }
            .flatMap { xy ->
                (min.z..max.z).map { z -> NeoVec3i(xy.first, xy.second, z) }
            }
            .iterator()
    }
}