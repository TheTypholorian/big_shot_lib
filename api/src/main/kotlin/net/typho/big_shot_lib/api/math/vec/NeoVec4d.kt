package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec2f
import org.joml.Vector4dc

class NeoVec4d(
    override val x: Double,
    override val y: Double,
    override val z: Double,
    override val w: Double
) : IVec4<Double> {
    constructor(other: IVec4<Double>) : this(other.x, other.y, other.z, other.w)

    constructor(other: Vector4dc) : this(other.x(), other.y(), other.z(), other.w())

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet
    override val xy: IVec2<Double>
        get() = NeoVec2d(x, y)
    override val yz: IVec2<Double>
        get() = NeoVec2d(y, z)
    override val zw: IVec2<Double>
        get() = NeoVec2d(z, w)
    override val xyz: IVec3<Double>
        get() = NeoVec3d(x, y, z)
    override val rg: IVec2<Double>
        get() = NeoVec2d(r, g)
    override val gb: IVec2<Double>
        get() = NeoVec2d(g, b)
    override val ba: IVec2<Double>
        get() = NeoVec2d(b, a)
    override val rgb: IVec3<Double>
        get() = NeoVec3d(r, g, b)

    override fun copyWith(x: Double, y: Double, z: Double, w: Double) = NeoVec4d(x, y, z, w)
}