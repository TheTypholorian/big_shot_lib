package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.NeoVec2d

class NeoRect2d : AbstractRect2<Double> {
    constructor(min: AbstractVec2<Double>, max: AbstractVec2<Double>) : super(min, max)

    constructor(x1: Double, y1: Double, x2: Double, y2: Double) : super(NeoVec2d(x1, y1), NeoVec2d(x2, y2))

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun create(min: AbstractVec2<Double>, max: AbstractVec2<Double>) = NeoRect2d(min, max)
}