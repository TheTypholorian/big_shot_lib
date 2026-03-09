package net.typho.big_shot_lib.api.math.minecraft

import net.minecraft.core.BlockBox
import net.minecraft.core.BlockPos
import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.rect.AbstractRect3

class NeoBlockBox(
    min: NeoBlockPos,
    max: NeoBlockPos
) : AbstractRect3<Int, NeoBlockBox, NeoBlockPos>(min, max), Iterable<NeoBlockPos> {
    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    val mojang: BlockBox
        get() = BlockBox(
            BlockPos(min.x, min.y, min.z),
            BlockPos(max.x, max.y, max.z)
        )

    override fun iterator(): Iterator<NeoBlockPos> {
        TODO("Not yet implemented")
    }
}