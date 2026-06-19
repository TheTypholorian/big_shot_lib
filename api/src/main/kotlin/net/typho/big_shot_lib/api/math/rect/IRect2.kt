package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec2

interface IRect2<N : Number> {
    val opSet: OperatorSet<N>

    val min: IVec2<N>
    val max: IVec2<N>

    val size: IVec2<N>
        get() = max - min
    val sizeInclusive: IVec2<N>
        get() = size + opSet.one
    val area: N
        get() {
            val size = size
            return opSet.times(size.x, size.y)
        }
    val areaInclusive: N
        get() {
            val size = sizeInclusive
            return opSet.times(size.x, size.y)
        }

    fun copyWith(min: IVec2<N>, max: IVec2<N>): IRect2<N>

    fun include(other: IRect2<N>): IRect2<N> {
        return copyWith(min.min(other.min), max.max(other.max))
    }

    fun include(other: IVec2<N>): IRect2<N> {
        return copyWith(min.min(other), max.max(other))
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

    operator fun iterator(): Iterator<IVec2<N>> {
        return iterator(opSet.one)
    }

    fun iterator(inc: N) = object : Iterator<IVec2<N>> {
        var x = min.x
        var y = min.y

        override fun hasNext(): Boolean {
            return opSet.lequalThan(x, max.x)
        }

        override fun next(): IVec2<N> {
            val pos = min.copyWith(x, y)

            y = opSet.plus(y, inc)

            if (opSet.greaterThan(y, max.y)) {
                y = min.y
                x = opSet.plus(x, inc)
            }

            return pos
        }
    }
}