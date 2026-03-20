package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector4i

class NeoVec4i : AbstractVec4<Int> {
    constructor(x: Int, y: Int, z: Int, w: Int) : super(x, y, z, w)

    constructor(other: AbstractVec4<Int>) : super(other)

    constructor(other: Vector4i) : super(other.x, other.y, other.z, other.w)

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet
    override val xy: AbstractVec2<Int>
        get() = NeoVec2i(x, y)
    override val yz: AbstractVec2<Int>
        get() = NeoVec2i(y, z)
    override val zw: AbstractVec2<Int>
        get() = NeoVec2i(z, w)
    override val xyz: AbstractVec3<Int>
        get() = NeoVec3i(x, y, z)
    override val rg: AbstractVec2<Int>
        get() = NeoVec2i(r, g)
    override val gb: AbstractVec2<Int>
        get() = NeoVec2i(g, b)
    override val ba: AbstractVec2<Int>
        get() = NeoVec2i(b, a)
    override val rgb: AbstractVec3<Int>
        get() = NeoVec3i(r, g, b)

    override fun create(x: Int, y: Int, z: Int, w: Int) = NeoVec4i(x, y, z, w)
}