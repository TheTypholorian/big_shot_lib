package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector2ic

class NeoVec2i : AbstractVec2<Int> {
    constructor(x: Int, y: Int) : super(x, y)

    constructor(other: AbstractVec2<Int>) : super(other)

    constructor(other: Vector2ic) : super(other.x(), other.y())

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    override fun create(x: Int, y: Int) = NeoVec2i(x, y)
}