package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector4d

class NeoVec4d : AbstractVec4<Double, NeoVec4d> {
    constructor(x: Double, y: Double, z: Double, w: Double) : super(x, y, z, w)

    constructor(other: AbstractVec4<Double, *>) : super(other)

    constructor(other: Vector4d) : super(other.x, other.y, other.z, other.w)

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet

    override fun create(x: Double, y: Double, z: Double, w: Double) = NeoVec4d(x, y, z, w)
}