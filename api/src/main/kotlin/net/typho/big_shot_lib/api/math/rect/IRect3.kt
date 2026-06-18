package net.typho.big_shot_lib.api.math.rect

import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec3

interface IRect3<N : Number> {
    val opSet: OperatorSet<N>

    val min: IVec3<N>
    val max: IVec3<N>

    val size: IVec3<N>
        get() = max - min
    val area: N
        get() = opSet.times(size.x, opSet.times(size.y, size.z))

    fun create(min: IVec3<N>, max: IVec3<N>): IRect3<N>

    fun include(other: IRect3<N>): IRect3<N> {
        return create(min.min(other.min), max.max(other.max))
    }

    fun include(other: IVec3<N>): IRect3<N> {
        return create(min.min(other), max.max(other))
    }

    fun contains(other: IRect3<N>): Boolean {
        return min.allLequalThan(other.min) && max.allGequalThan(other.max)
    }

    fun contains(other: IVec3<N>): Boolean {
        return min.allLequalThan(other) && max.allGequalThan(other)
    }

    fun intersects(other: IRect3<N>): Boolean {
        return min.allLessThan(other.max) && max.allGreaterThan(other.min)
    }

    companion object {
        @JvmStatic
        val <N : Number> IRect3<N>.sizeInclusive: IVec3<N>
            get() = size + opSet.one

        @JvmStatic
        val IRect3<Int>.areaInclusive: Int
            get() = opSet.times(size.x + 1, opSet.times(size.y + 1, size.z + 1))

        @JvmStatic
        operator fun IRect3<Int>.iterator(): Iterator<IVec3<Int>> = object : Iterator<IVec3<Int>> {
            var x = min.x
            var y = min.y
            var z = min.z

            override fun hasNext(): Boolean {
                return x <= max.x
            }

            override fun next(): IVec3<Int> {
                val pos = IVec3(x, y, z)

                z++

                if (z > max.z) {
                    z = min.z
                    y++

                    if (y > max.y) {
                        y = min.y
                        x++
                    }
                }

                return pos
            }
        }

        @JvmStatic
        fun IRect3<Int>.extend(direction: Direction): IRect3<Int> {
            return if (direction.axisDirection == Direction.AxisDirection.POSITIVE) {
                create(min, max + direction)
            } else {
                create(min + direction, max)
            }
        }

        @JvmStatic
        fun IRect3<Int>.move(direction: Direction) = create(min + direction, max + direction)
    }
}