package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec4f

class NeoRect4f : AbstractRect4<Float, NeoRect4f, NeoVec4f> {
    constructor(min: NeoVec4f, max: NeoVec4f) : super(min, max)

    constructor(x1: Float, y1: Float, z1: Float, w1: Float, x2: Float, y2: Float, z2: Float, w2: Float) : super(NeoVec4f(x1, y1, z1, w1), NeoVec4f(x2, y2, z2, w2))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(min: NeoVec4f, max: NeoVec4f) = NeoRect4f(min, max)
}