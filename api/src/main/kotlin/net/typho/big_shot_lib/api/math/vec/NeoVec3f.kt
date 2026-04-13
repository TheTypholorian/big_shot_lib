package net.typho.big_shot_lib.api.math.vec

import net.minecraft.world.phys.Vec3
import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3fc

class NeoVec3f(
    override val x: Float,
    override val y: Float,
    override val z: Float
) : IVec3<Float> {
    constructor(other: IVec3<Float>) : this(other.x, other.y, other.z)

    constructor(other: Vector3fc) : this(other.x(), other.y(), other.z())

    constructor(other: Vec3) : this(other.x.toFloat(), other.y.toFloat(), other.z.toFloat())

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet
    override val xy: IVec2<Float>
        get() = NeoVec2f(x, y)
    override val yz: IVec2<Float>
        get() = NeoVec2f(y, z)
    override val xz: IVec2<Float>
        get() = NeoVec2f(x, z)

    override fun copyWith(x: Float, y: Float, z: Float) = NeoVec3f(x, y, z)
}