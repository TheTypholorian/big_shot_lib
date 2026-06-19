package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec4

class NeoRect4i(
    override val min: IVec4<Int>,
    override val max: IVec4<Int>
) : IRect4<Int> {
    constructor(x1: Int, y1: Int, z1: Int, w1: Int, x2: Int, y2: Int, z2: Int, w2: Int) : this(IVec4(x1, y1, z1, w1), IVec4(x2, y2, z2, w2))

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    override fun copyWith(min: IVec4<Int>, max: IVec4<Int>) = NeoRect4i(min, max)
}