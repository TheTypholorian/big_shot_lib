package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec2

interface IRect2<N : Number> {
    val opSet: OperatorSet<N>

    val min: IVec2<N>
    val max: IVec2<N>

    val size: IVec2<N>
        get() = max - min
    val area: N
        get() = opSet.times(size.x, size.y)

    fun create(min: IVec2<N>, max: IVec2<N>): IRect2<N>

    fun include(other: IRect2<N>): IRect2<N> {
        return create(min.min(other.min), max.max(other.max))
    }

    fun include(other: IVec2<N>): IRect2<N> {
        return create(min.min(other), max.max(other))
    }

    fun contains(other: IRect2<N>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: IVec2<N>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: IRect2<N>): Boolean {
        return min.allLessThan(other.max) && max.allGreaterThan(other.min)
    }

    companion object {
        @JvmStatic
        val <N : Number> IRect2<N>.sizeInclusive: IVec2<N>
            get() = size + opSet.one

        @JvmStatic
        val IRect2<Int>.areaInclusive: Int
            get() = opSet.times(size.x + 1, size.y + 1)

        @JvmStatic
        operator fun IRect2<Int>.iterator(): Iterator<IVec2<Int>> = (min.x..max.x)
            .flatMap { x ->
                (min.y..max.y).map { y -> IVec2(x, y) }
            }
            .iterator()
    }
}