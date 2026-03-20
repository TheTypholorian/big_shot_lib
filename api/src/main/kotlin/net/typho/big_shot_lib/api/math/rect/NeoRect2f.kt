package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.NeoVec2f

class NeoRect2f : AbstractRect2<Float> {
    constructor(min: AbstractVec2<Float>, max: AbstractVec2<Float>) : super(min, max)

    constructor(x1: Float, y1: Float, x2: Float, y2: Float) : super(NeoVec2f(x1, y1), NeoVec2f(x2, y2))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(min: AbstractVec2<Float>, max: AbstractVec2<Float>) = NeoRect2f(min, max)
}