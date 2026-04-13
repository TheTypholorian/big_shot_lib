package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.FloatOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec2f
import org.joml.Vector4fc

class NeoVec4f(
    override val x: Float,
    override val y: Float,
    override val z: Float,
    override val w: Float
) : IVec4<Float> {
    constructor(other: IVec4<Float>) : this(other.x, other.y, other.z, other.w)

    constructor(other: Vector4fc) : this(other.x(), other.y(), other.z(), other.w())

    override val opSet: OperatorSet<Float>
        get() = FloatOperatorSet
    override val xy: IVec2<Float>
        get() = NeoVec2f(x, y)
    override val yz: IVec2<Float>
        get() = NeoVec2f(y, z)
    override val zw: IVec2<Float>
        get() = NeoVec2f(z, w)
    override val xyz: IVec3<Float>
        get() = NeoVec3f(x, y, z)
    override val rg: IVec2<Float>
        get() = NeoVec2f(r, g)
    override val gb: IVec2<Float>
        get() = NeoVec2f(g, b)
    override val ba: IVec2<Float>
        get() = NeoVec2f(b, a)
    override val rgb: IVec3<Float>
        get() = NeoVec3f(r, g, b)

    override fun copyWith(x: Float, y: Float, z: Float, w: Float) = NeoVec4f(x, y, z, w)

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