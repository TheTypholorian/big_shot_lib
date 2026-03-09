package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3d

class NeoVec3d : AbstractVec3<Double, NeoVec3d> {
    constructor(x: Double, y: Double, z: Double) : super(x, y, z)

    constructor(other: AbstractVec3<Double, *>) : super(other)

    constructor(other: Vector3d) : super(other.x, other.y, other.z)

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun create(x: Double, y: Double, z: Double) = NeoVec3d(x, y, z)
}