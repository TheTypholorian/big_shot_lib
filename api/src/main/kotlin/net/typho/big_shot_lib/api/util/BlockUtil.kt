package net.typho.big_shot_lib.api.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.client.rendering.util.BlockChunkLayer
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.math.vec.IVec3

interface BlockUtil {
    fun isSolidRender(state: BlockState, pos: IVec3<Int>, level: Level): Boolean

    fun getOffset(state: BlockState, pos: IVec3<Int>, level: Level): IVec3<Float>

    fun getBlockChunkLayer(state: BlockState): BlockChunkLayer?

    fun getFluidRenderSettings(state: FluidState): NeoRenderType

    fun shouldRenderFace(
        level: BlockGetter,
        pos: IVec3<Int>,
        direction: Direction,
        state: BlockState = level.getBlockState(BlockPos(pos.x, pos.y, pos.z))
    ): Boolean

    fun getBlockQuads(
        state: BlockState,
        level: Level,
        pos: IVec3<Int>,
        out: (direction: Direction?, quads: List<NeoBakedQuad>) -> Unit
    )

    fun getFluidQuads(
        state: BlockState,
        fluid: FluidState,
        level: Level,
        pos: IVec3<Int>,
        occlusionCheck: (level: BlockGetter, from: BlockPos, direction: Direction, otherState: BlockState) -> Boolean,
        out: (quad: NeoBakedQuad) -> Unit
    )

    companion object {
        @JvmStatic
        @get:JvmName("getInstance")
        val INSTANCE by lazy { BlockUtil::class.loadService() }
    }
}