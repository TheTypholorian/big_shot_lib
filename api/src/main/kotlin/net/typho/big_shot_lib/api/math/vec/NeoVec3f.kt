package net.typho.big_shot_lib.api.math.vec

import net.minecraft.world.phys.Vec3
import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3fc

class NeoVec3f : AbstractVec3<Float> {
    constructor(x: Float, y: Float, z: Float) : super(x, y, z)

    constructor(other: AbstractVec3<Float>) : super(other)

    constructor(other: Vector3fc) : super(other.x(), other.y(), other.z())

    constructor(other: Vec3) : super(other.x.toFloat(), other.y.toFloat(), other.z.toFloat())

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet
    override val xy: AbstractVec2<Float>
        get() = NeoVec2f(x, y)
    override val yz: AbstractVec2<Float>
        get() = NeoVec2f(y, z)
    override val xz: AbstractVec2<Float>
        get() = NeoVec2f(x, z)
    override val rg: AbstractVec2<Float>
        get() = NeoVec2f(r, g)
    override val gb: AbstractVec2<Float>
        get() = NeoVec2f(g, b)
    override val rb: AbstractVec2<Float>
        get() = NeoVec2f(r, b)

    override fun create(x: Float, y: Float, z: Float) = NeoVec3f(x, y, z)
}