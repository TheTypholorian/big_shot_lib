package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.util.floatAt
import org.joml.Vector2f

class NeoVec2f : AbstractVec2<Float> {
    constructor(x: Float, y: Float) : super(x, y)

    constructor(other: AbstractVec2<Float>) : super(other)

    constructor(other: Vector2f) : super(other.x, other.y)

    constructor(packed: Long) : super(packed.floatAt(1), packed.floatAt(0))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(x: Float, y: Float) = NeoVec2f(x, y)
}