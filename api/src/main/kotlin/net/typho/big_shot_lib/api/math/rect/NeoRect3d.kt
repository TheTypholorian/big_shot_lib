package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec3d

class NeoRect3d : AbstractRect3<Double, NeoRect3d, NeoVec3d> {
    constructor(min: NeoVec3d, max: NeoVec3d) : super(min, max)

    constructor(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) : super(NeoVec3d(x1, y1, z1), NeoVec3d(x2, y2, z2))

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun create(min: NeoVec3d, max: NeoVec3d) = NeoRect3d(min, max)
}