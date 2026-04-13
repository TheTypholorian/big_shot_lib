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
}