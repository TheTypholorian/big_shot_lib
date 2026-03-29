package net.typho.big_shot_lib.api.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.client.rendering.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.client.rendering.util.BlockChunkLayer
import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.math.vec.AbstractVec3

interface BlockUtil {
    fun isSolidRender(state: BlockState, pos: AbstractVec3<Int>, level: Level): Boolean

    fun getOffset(state: BlockState, pos: AbstractVec3<Int>, level: Level): AbstractVec3<Float>

    fun getBlockChunkLayer(state: BlockState): BlockChunkLayer?

    fun shouldRenderFace(
        level: BlockGetter,
        pos: AbstractVec3<Int>,
        direction: NeoDirection,
        state: BlockState = level.getBlockState(BlockPos(pos.x, pos.y, pos.z))
    ): Boolean

    fun getBlockQuads(
        state: BlockState,
        level: Level,
        pos: AbstractVec3<Int>,
        out: (direction: NeoDirection?, quads: List<NeoBakedQuad>) -> Unit
    )

    companion object {
        val INSTANCE by lazy { BlockUtil::class.loadService() }
    }
}