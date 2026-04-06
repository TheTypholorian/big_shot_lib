package net.typho.big_shot_lib.impl.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.client.rendering.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.math.NeoDirection

class FluidQuadConsumer(
    @JvmField
    val occlusionCheck: (level: BlockGetter, from: BlockPos, direction: NeoDirection, otherState: BlockState) -> Boolean,
    private val out: (quad: NeoBakedQuad) -> Unit
) : NeoBakedQuad.Consumer() {
    @JvmField
    var direction: NeoDirection = NeoDirection.UP

    override fun take(quad: NeoBakedQuad) {
        out(quad.withDirection { direction }.withCalculatedNormals())
    }
}