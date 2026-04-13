package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec2f
import org.joml.Vector2ic

class NeoVec2i(
    override val x: Int,
    override val y: Int
) : IVec2<Int> {
    constructor(other: IVec2<Int>) : this(other.x, other.y)

    constructor(other: Vector2ic) : this(other.x(), other.y())

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    override fun copyWith(x: Int, y: Int) = NeoVec2i(x, y)
}