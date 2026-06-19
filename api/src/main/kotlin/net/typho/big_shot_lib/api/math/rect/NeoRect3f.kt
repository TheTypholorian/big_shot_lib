package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec3

class NeoRect3f(
    override val min: IVec3<Float>,
    override val max: IVec3<Float>
) : IRect3<Float> {
    constructor(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float) : this(IVec3(x1, y1, z1), IVec3(x2, y2, z2))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun copyWith(min: IVec3<Float>, max: IVec3<Float>) = NeoRect3f(min, max)
}