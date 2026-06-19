package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec3

class NeoRect3d(
    override val min: IVec3<Double>,
    override val max: IVec3<Double>
) : IRect3<Double> {
    constructor(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) : this(IVec3(x1, y1, z1), IVec3(x2, y2, z2))

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun copyWith(min: IVec3<Double>, max: IVec3<Double>) = NeoRect3d(min, max)
}