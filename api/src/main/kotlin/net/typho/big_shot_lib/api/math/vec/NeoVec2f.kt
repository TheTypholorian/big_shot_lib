package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec2d
import net.typho.big_shot_lib.api.util.buffer.floatAt
import org.joml.Vector2fc

class NeoVec2f(
    override val x: Float,
    override val y: Float
) : IVec2<Float> {
    constructor(other: IVec2<Float>) : this(other.x, other.y)

    constructor(other: Vector2fc) : this(other.x(), other.y())

    constructor(packed: Long) : this(packed.floatAt(1), packed.floatAt(0))

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun copyWith(x: Float, y: Float) = NeoVec2f(x, y)
}