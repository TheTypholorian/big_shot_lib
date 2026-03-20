package net.typho.big_shot_lib.api.math.rect

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.AbstractVec4
import net.typho.big_shot_lib.api.math.vec.NeoVec4i

class NeoRect4i : AbstractRect4<Int> {
    constructor(min: AbstractVec4<Int>, max: AbstractVec4<Int>) : super(min, max)

    constructor(x1: Int, y1: Int, z1: Int, w1: Int, x2: Int, y2: Int, z2: Int, w2: Int) : super(NeoVec4i(x1, y1, z1, w1), NeoVec4i(x2, y2, z2, w2))

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    override fun create(min: AbstractVec4<Int>, max: AbstractVec4<Int>) = NeoRect4i(min, max)
}