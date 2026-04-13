package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3d

class NeoRect3d : AbstractRect3<Double> {
    constructor(min: IVec3<Double>, max: IVec3<Double>) : super(min, max)

    constructor(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) : super(NeoVec3d(x1, y1, z1), NeoVec3d(x2, y2, z2))

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun create(min: IVec3<Double>, max: IVec3<Double>) = NeoRect3d(min, max)
}