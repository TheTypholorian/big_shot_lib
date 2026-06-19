package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec2

class NeoRect2i(
    override val min: IVec2<Int>,
    override val max: IVec2<Int>
) : IRect2<Int> {
    constructor(x1: Int, y1: Int, x2: Int, y2: Int) : this(IVec2(x1, y1), IVec2(x2, y2))

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    override fun copyWith(min: IVec2<Int>, max: IVec2<Int>) = NeoRect2i(min, max)
}