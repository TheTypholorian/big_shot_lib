package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector4f

class NeoVec4f : AbstractVec4<Float> {
    constructor(x: Float, y: Float, z: Float, w: Float) : super(x, y, z, w)

    constructor(other: AbstractVec4<Float>) : super(other)

    constructor(other: Vector4f) : super(other.x, other.y, other.z, other.w)

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet
    override val xy: AbstractVec2<Float>
        get() = NeoVec2f(x, y)
    override val yz: AbstractVec2<Float>
        get() = NeoVec2f(y, z)
    override val zw: AbstractVec2<Float>
        get() = NeoVec2f(z, w)
    override val xyz: AbstractVec3<Float>
        get() = NeoVec3f(x, y, z)
    override val rg: AbstractVec2<Float>
        get() = NeoVec2f(r, g)
    override val gb: AbstractVec2<Float>
        get() = NeoVec2f(g, b)
    override val ba: AbstractVec2<Float>
        get() = NeoVec2f(b, a)
    override val rgb: AbstractVec3<Float>
        get() = NeoVec3f(r, g, b)

    override fun create(x: Float, y: Float, z: Float, w: Float) = NeoVec4f(x, y, z, w)
}