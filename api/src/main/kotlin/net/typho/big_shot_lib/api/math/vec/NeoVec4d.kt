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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IVec4<*>) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (w != other.w) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 41 * result + y.hashCode()
        result = 41 * result + z.hashCode()
        result = 41 * result + w.hashCode()
        return result
    }

    override fun toString(): String {
        return "(x=$x, y=$y, z=$z, w=$w)"
    }
}