package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec2f
import org.joml.Vector2ic

class NeoVec2i(
    override val x: Int,
    override val y: Int
) : IVec2<Int> {
    constructor(other: IVec2<Int>) : this(other.x, other.y)

    constructor(other: Vector2ic) : this(other.x(), other.y())

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    override fun copyWith(x: Int, y: Int) = NeoVec2i(x, y)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IVec3<*>) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String {
        return "(x=$x, y=$y)"
    }
}