package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec2d
import net.typho.big_shot_lib.api.util.buffer.floatAt
import org.joml.Vector2fc

class NeoVec2f(
    override val x: Float,
    override val y: Float
) : IVec2<Float> {
    constructor(other: IVec2<Float>) : this(other.x, other.y)

    constructor(other: Vector2fc) : this(other.x(), other.y())

    constructor(packed: Long) : this(packed.floatAt(1), packed.floatAt(0))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun copyWith(x: Float, y: Float) = NeoVec2f(x, y)

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