package net.typho.big_shot_lib.api.math.minecraft

import net.minecraft.core.BlockBox
import net.minecraft.core.BlockPos
import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.rect.AbstractRect3

class NeoBlockBox : AbstractRect3<Int, NeoBlockBox, NeoBlockPos>, Iterable<NeoBlockPos> {
    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet

    val mojang: BlockBox
        get() = BlockBox(
            BlockPos(min.x, min.y, min.z),
            BlockPos(max.x, max.y, max.z)
        )

    constructor(min: NeoBlockPos, max: NeoBlockPos) : super(min, max)

    constructor(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) : super(NeoBlockPos(x1, y1, z1), NeoBlockPos(x2, y2, z2))

    override fun create(min: NeoBlockPos, max: NeoBlockPos) = NeoBlockBox(min, max)

    override fun iterator(): Iterator<NeoBlockPos> {
        return (min.x..max.x)
            .flatMap { x ->
                (min.y..max.y).map { y -> x to y }
            }
            .flatMap { xy ->
                (min.z..max.z).map { z -> NeoBlockPos(xy.first, xy.second, z) }
            }
            .iterator()
    }
}