package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector4f

class NeoVec4f : AbstractVec4<Float, NeoVec4f> {
    constructor(x: Float, y: Float, z: Float, w: Float) : super(x, y, z, w)

    constructor(other: AbstractVec4<Float, *>) : super(other)

    constructor(other: Vector4f) : super(other.x, other.y, other.z, other.w)

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(x: Float, y: Float, z: Float, w: Float) = NeoVec4f(x, y, z, w)
}