package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec2

class NeoRect2f(
    override val min: IVec2<Float>,
    override val max: IVec2<Float>
) : IRect2<Float> {
    constructor(x1: Float, y1: Float, x2: Float, y2: Float) : this(IVec2(x1, y1), IVec2(x2, y2))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(min: IVec2<Float>, max: IVec2<Float>) = NeoRect2f(min, max)
}