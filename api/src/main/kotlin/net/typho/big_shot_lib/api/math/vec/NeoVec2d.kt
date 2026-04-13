package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector2dc

class NeoVec2d(
    override val x: Double,
    override val y: Double
) : IVec2<Double> {
    constructor(other: IVec2<Double>) : this(other.x, other.y)

    constructor(other: Vector2dc) : this(other.x(), other.y())

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun copyWith(x: Double, y: Double) = NeoVec2d(x, y)

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