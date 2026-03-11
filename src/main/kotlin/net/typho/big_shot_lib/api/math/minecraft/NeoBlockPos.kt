package net.typho.big_shot_lib.api.math.minecraft

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import org.joml.Vector3i

class NeoBlockPos : AbstractVec3<Int, NeoBlockPos> {
    constructor(x: Int, y: Int, z: Int) : super(x, y, z)

    constructor(other: AbstractVec3i) : super(other)

    constructor(other: Vec3i) : super(other.x, other.y, other.z)

    constructor(other: Vector3i) : super(other.x, other.y, other.z)

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    val center: AbstractVec3f
        get() = NeoVec3f(x + 0.5f, y + 0.5f, z + 0.5f)
    val mojang: BlockPos
        get() = BlockPos(x, y, z)

    infix fun to(other: NeoBlockPos): NeoBlockBox = NeoBlockBox(this, other)

    override fun create(x: Int, y: Int, z: Int) = NeoBlockPos(x, y, z)
}