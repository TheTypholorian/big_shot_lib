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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IVec3<*>) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun toString(): String {
        return "(x=$x, y=$y, z=$z)"
    }
}