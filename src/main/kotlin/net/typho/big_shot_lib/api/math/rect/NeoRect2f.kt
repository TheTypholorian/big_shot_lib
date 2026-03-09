package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec2f

class NeoRect2f : AbstractRect2<Float, NeoRect2f, NeoVec2f> {
    constructor(min: NeoVec2f, max: NeoVec2f) : super(min, max)

    constructor(x1: Float, y1: Float, x2: Float, y2: Float) : super(NeoVec2f(x1, y1), NeoVec2f(x2, y2))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(min: NeoVec2f, max: NeoVec2f) = NeoRect2f(min, max)
}