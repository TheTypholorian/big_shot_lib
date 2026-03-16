package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector2f

class NeoVec2f : AbstractVec2<Float, NeoVec2f> {
    constructor(x: Float, y: Float) : super(x, y)

    constructor(other: AbstractVec2<Float, *>) : super(other)

    constructor(other: Vector2f) : super(other.x, other.y)

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(x: Float, y: Float) = NeoVec2f(x, y)
}