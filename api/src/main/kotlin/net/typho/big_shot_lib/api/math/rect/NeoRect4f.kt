package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec4

class NeoRect4f(
    override val min: IVec4<Float>,
    override val max: IVec4<Float>
) : IRect4<Float> {
    constructor(x1: Float, y1: Float, z1: Float, w1: Float, x2: Float, y2: Float, z2: Float, w2: Float) : this(IVec4(x1, y1, z1, w1), IVec4(x2, y2, z2, w2))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(min: IVec4<Float>, max: IVec4<Float>) = NeoRect4f(min, max)
}