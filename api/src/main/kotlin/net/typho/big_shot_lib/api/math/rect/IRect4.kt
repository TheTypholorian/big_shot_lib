package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec4
import net.typho.big_shot_lib.api.math.vec.NeoVec4i

interface IRect4<N : Number> {
    val opSet: OperatorSet<N>

    val min: IVec4<N>
    val max: IVec4<N>

    val size: IVec4<N>
        get() = max - min
    val area: N
        get() = opSet.times(size.x, opSet.times(size.y, opSet.times(size.z, size.w)))

    fun create(min: IVec4<N>, max: IVec4<N>): IRect4<N>

    fun include(other: IRect4<N>): IRect4<N> {
        return create(min.min(other.min), max.max(other.max))
    }

    fun include(other: IVec4<N>): IRect4<N> {
        return create(min.min(other), max.max(other))
    }

    fun contains(other: IRect4<N>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: IVec4<N>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: IRect4<N>): Boolean {
        return min.allLessThan(other.max) && max.allGreaterThan(other.min)
    }

    companion object {
        @JvmStatic
        val <N : Number> IRect4<N>.sizeInclusive: IVec4<N>
            get() = size + opSet.one

        @JvmStatic
        val IRect4<Int>.areaInclusive: Int
            get() = opSet.times(size.x + 1, opSet.times(size.y + 1, opSet.times(size.z + 1, size.w + 1)))

        @JvmStatic
        operator fun IRect4<Int>.iterator(): Iterator<IVec4<Int>> = (min.x..max.x)
            .flatMap { x ->
                (min.y..max.y).map { y -> x to y }
            }
            .flatMap { xy ->
                (min.z..max.z).map { z -> xy to z }
            }
            .flatMap { xyz ->
                (min.z..max.z).map { w -> NeoVec4i(xyz.first.first, xyz.first.second, xyz.second, w) }
            }
            .iterator()
    }
}