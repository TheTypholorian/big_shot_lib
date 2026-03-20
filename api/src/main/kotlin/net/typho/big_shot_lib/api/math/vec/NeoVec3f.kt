package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3fc

class NeoVec3f : AbstractVec3<Float> {
    constructor(x: Float, y: Float, z: Float) : super(x, y, z)

    constructor(other: AbstractVec3<Float>) : super(other)

    constructor(other: Vector3fc) : super(other.x()(), other.y()(), other.z()())

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet
    override val xy: AbstractVec2<Float>
        get() = NeoVec2f(x, y)
    override val yz: AbstractVec2<Float>
        get() = NeoVec2f(y, z)
    override val rg: AbstractVec2<Float>
        get() = NeoVec2f(r, g)
    override val gb: AbstractVec2<Float>
        get() = NeoVec2f(g, b)

    override fun create(x: Float, y: Float, z: Float) = NeoVec3f(x, y, z)
}