package net.typho.big_shot_lib.api.math.vec

import net.minecraft.core.Vec3i
import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3ic

class NeoVec3i : AbstractVec3<Int> {
    constructor(x: Int, y: Int, z: Int) : super(x, y, z)

    constructor(other: AbstractVec3<Int>) : super(other)

    constructor(other: Vec3i) : super(other.x, other.y, other.z)

    constructor(other: Vector3ic) : super(other.x(), other.y(), other.z())

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet
    override val xy: AbstractVec2<Int>
        get() = NeoVec2i(x, y)
    override val yz: AbstractVec2<Int>
        get() = NeoVec2i(y, z)
    override val xz: AbstractVec2<Int>
        get() = NeoVec2i(x, z)
    override val rg: AbstractVec2<Int>
        get() = NeoVec2i(r, g)
    override val gb: AbstractVec2<Int>
        get() = NeoVec2i(g, b)
    override val rb: AbstractVec2<Int>
        get() = NeoVec2i(r, b)

    override fun create(x: Int, y: Int, z: Int) = NeoVec3i(x, y, z)
}