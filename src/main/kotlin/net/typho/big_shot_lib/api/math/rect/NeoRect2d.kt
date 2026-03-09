package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec2d

class NeoRect2d : AbstractRect2<Double, NeoRect2d, NeoVec2d> {
    constructor(min: NeoVec2d, max: NeoVec2d) : super(min, max)

    constructor(x1: Double, y1: Double, x2: Double, y2: Double) : super(NeoVec2d(x1, y1), NeoVec2d(x2, y2))

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun create(min: NeoVec2d, max: NeoVec2d) = NeoRect2d(min, max)
}