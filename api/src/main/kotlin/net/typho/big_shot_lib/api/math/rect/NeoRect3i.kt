package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec3

class NeoRect3i(
    override val min: IVec3<Int>,
    override val max: IVec3<Int>
) : IRect3<Int> {
    constructor(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) : this(IVec3(x1, y1, z1), IVec3(x2, y2, z2))

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    override fun copyWith(min: IVec3<Int>, max: IVec3<Int>) = NeoRect3i(min, max)
}