package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec3f

class NeoRect3f : AbstractRect3<Float, NeoRect3f, NeoVec3f> {
    constructor(min: NeoVec3f, max: NeoVec3f) : super(min, max)

    constructor(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float) : super(NeoVec3f(x1, y1, z1), NeoVec3f(x2, y2, z2))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(min: NeoVec3f, max: NeoVec3f) = NeoRect3f(min, max)
}