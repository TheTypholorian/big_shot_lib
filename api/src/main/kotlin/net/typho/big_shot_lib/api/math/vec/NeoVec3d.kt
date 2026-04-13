package net.typho.big_shot_lib.api.math.vec

import net.minecraft.world.phys.Vec3
import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3dc

class NeoVec3d(
    override val x: Double,
    override val y: Double,
    override val z: Double
) : IVec3<Double> {
    constructor(other: IVec3<Double>) : this(other.x, other.y, other.z)

    constructor(other: Vector3dc) : this(other.x(), other.y(), other.z())

    constructor(other: Vec3) : this(other.x, other.y, other.z)

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet
    override val xy: IVec2<Double>
        get() = NeoVec2d(x, y)
    override val yz: IVec2<Double>
        get() = NeoVec2d(y, z)
    override val xz: IVec2<Double>
        get() = NeoVec2d(x, z)

    override fun copyWith(x: Double, y: Double, z: Double) = NeoVec3d(x, y, z)
}