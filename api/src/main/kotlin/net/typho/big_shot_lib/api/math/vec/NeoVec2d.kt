package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector2d

class NeoVec2d : AbstractVec2<Double, NeoVec2d> {
    constructor(x: Double, y: Double) : super(x, y)

    constructor(other: AbstractVec2<Double, *>) : super(other)

    constructor(other: Vector2d) : super(other.x, other.y)

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun create(x: Double, y: Double) = NeoVec2d(x, y)
}