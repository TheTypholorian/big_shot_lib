package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec3i

class NeoRect3i : AbstractRect3<Int, NeoRect3i, NeoVec3i> {
    constructor(min: NeoVec3i, max: NeoVec3i) : super(min, max)

    constructor(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) : super(NeoVec3i(x1, y1, z1), NeoVec3i(x2, y2, z2))

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    override fun create(min: NeoVec3i, max: NeoVec3i) = NeoRect3i(min, max)
}