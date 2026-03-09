package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector4i

class NeoVec4i : AbstractVec4<Int, NeoVec4i> {
    constructor(x: Int, y: Int, z: Int, w: Int) : super(x, y, z, w)

    constructor(other: AbstractVec4<Int, *>) : super(other)

    constructor(other: Vector4i) : super(other.x, other.y, other.z, other.w)

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    override fun create(x: Int, y: Int, z: Int, w: Int) = NeoVec4i(x, y, z, w)
}