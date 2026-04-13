package net.typho.big_shot_lib.api.math.vec

import net.minecraft.core.Vec3i
import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import org.joml.Vector3ic

class NeoVec3i(
    override val x: Int,
    override val y: Int,
    override val z: Int
) : IVec3<Int> {
    constructor(other: IVec3<Int>) : this(other.x, other.y, other.z)

    constructor(other: Vec3i) : this(other.x, other.y, other.z)

    constructor(other: Vector3ic) : this(other.x(), other.y(), other.z())

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet
    override val xy: IVec2<Int>
        get() = NeoVec2i(x, y)
    override val yz: IVec2<Int>
        get() = NeoVec2i(y, z)
    override val xz: IVec2<Int>
        get() = NeoVec2i(x, z)

    override fun copyWith(x: Int, y: Int, z: Int) = NeoVec3i(x, y, z)
}