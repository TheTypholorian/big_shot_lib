package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3dc

class NeoVec3d : AbstractVec3<Double> {
    constructor(x: Double, y: Double, z: Double) : super(x, y, z)

    constructor(other: AbstractVec3<Double>) : super(other)

    constructor(other: Vector3dc) : super(other.x(), other.y(), other.z())

    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet
    override val xy: AbstractVec2<Double>
        get() = NeoVec2d(x, y)
    override val yz: AbstractVec2<Double>
        get() = NeoVec2d(y, z)
    override val rg: AbstractVec2<Double>
        get() = NeoVec2d(r, g)
    override val gb: AbstractVec2<Double>
        get() = NeoVec2d(g, b)

    override fun create(x: Double, y: Double, z: Double) = NeoVec3d(x, y, z)
}