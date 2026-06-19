package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec4

class NeoRect4d(
    override val min: IVec4<Double>,
    override val max: IVec4<Double>
) : IRect4<Double> {
    constructor(x1: Double, y1: Double, z1: Double, w1: Double, x2: Double, y2: Double, z2: Double, w2: Double) : this(IVec4(x1, y1, z1, w1), IVec4(x2, y2, z2, w2))

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun copyWith(min: IVec4<Double>, max: IVec4<Double>) = NeoRect4d(min, max)
}