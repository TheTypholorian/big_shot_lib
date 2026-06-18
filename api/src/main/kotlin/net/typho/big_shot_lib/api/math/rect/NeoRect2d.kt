package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec2

class NeoRect2d(
    override val min: IVec2<Double>,
    override val max: IVec2<Double>
) : IRect2<Double> {
    constructor(x1: Double, y1: Double, x2: Double, y2: Double) : this(IVec2(x1, y1), IVec2(x2, y2))

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun create(min: IVec2<Double>, max: IVec2<Double>) = NeoRect2d(min, max)
}