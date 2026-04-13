package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec4
import net.typho.big_shot_lib.api.math.vec.NeoVec4d

class NeoRect4d : AbstractRect4<Double> {
    constructor(min: IVec4<Double>, max: IVec4<Double>) : super(min, max)

    constructor(x1: Double, y1: Double, z1: Double, w1: Double, x2: Double, y2: Double, z2: Double, w2: Double) : super(NeoVec4d(x1, y1, z1, w1), NeoVec4d(x2, y2, z2, w2))

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun create(min: IVec4<Double>, max: IVec4<Double>) = NeoRect4d(min, max)
}