package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3f

class NeoVec3f : AbstractVec3<Float, NeoVec3f> {
    constructor(x: Float, y: Float, z: Float) : super(x, y, z)

    constructor(other: AbstractVec3<Float, *>) : super(other)

    constructor(other: Vector3f) : super(other.x, other.y, other.z)

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet

    override fun create(x: Float, y: Float, z: Float) = NeoVec3f(x, y, z)
}