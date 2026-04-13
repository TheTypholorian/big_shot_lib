package net.typho.big_shot_lib.api.math.vec

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.NeoVec2f
import org.joml.Vector4ic

class NeoVec4i(
    override val x: Int,
    override val y: Int,
    override val z: Int,
    override val w: Int
) : IVec4<Int> {
    constructor(other: IVec4<Int>) : this(other.x, other.y, other.z, other.w)

    constructor(other: Vector4ic) : this(other.x(), other.y(), other.z(), other.w())

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet
    override val xy: IVec2<Int>
        get() = NeoVec2i(x, y)
    override val yz: IVec2<Int>
        get() = NeoVec2i(y, z)
    override val zw: IVec2<Int>
        get() = NeoVec2i(z, w)
    override val xyz: IVec3<Int>
        get() = NeoVec3i(x, y, z)
    override val rg: IVec2<Int>
        get() = NeoVec2i(r, g)
    override val gb: IVec2<Int>
        get() = NeoVec2i(g, b)
    override val ba: IVec2<Int>
        get() = NeoVec2i(b, a)
    override val rgb: IVec3<Int>
        get() = NeoVec3i(r, g, b)

    override fun copyWith(x: Int, y: Int, z: Int, w: Int) = NeoVec4i(x, y, z, w)
}