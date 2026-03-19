package net.typho.big_shot_lib.impl.util

//? if >=1.21.6 {
import net.minecraft.client.renderer.chunk.ChunkSectionLayer
//? } else {
/*import net.minecraft.client.renderer.RenderType
*///? }

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.client.util.BlockRenderSettings
import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.math.minecraft.NeoBlockPos
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.BlockUtil

object BlockUtilImpl : BlockUtil {
    override fun isSolidRender(
        state: BlockState,
        pos: NeoBlockPos,
        level: Level
    ): Boolean {
        //? if <1.21.2 {
        /*return state.isSolidRender(level, pos.mojang)
        *///? } else {
        return state.isSolidRender
        //? }
    }

    override fun getOffset(
        state: BlockState,
        pos: NeoBlockPos,
        level: Level
    ): AbstractVec3<Float, *> {
        //? if <1.21.2 {
        /*return NeoVec3f(state.getOffset(level, pos.mojang).toVector3f())
        *///? } else {
        return NeoVec3f(state.getOffset(pos.mojang).toVector3f())
        //? }
    }

    @Suppress("DEPRECATION")
    override fun getBlockRenderSettings(state: BlockState): BlockRenderSettings? {
        return when (ItemBlockRenderTypes.getChunkRenderType(state)) {
            //? if >=1.21.11 {
            /*ChunkSectionLayer.SOLID -> BlockRenderSettings.SOLID
            ChunkSectionLayer.CUTOUT -> BlockRenderSettings.CUTOUT
            ChunkSectionLayer.TRANSLUCENT -> BlockRenderSettings.TRANSLUCENT
            ChunkSectionLayer.TRIPWIRE -> BlockRenderSettings.TRIPWIRE
            *///? } else if >=1.21.6 {
            ChunkSectionLayer.SOLID -> BlockRenderSettings.SOLID
            ChunkSectionLayer.CUTOUT -> BlockRenderSettings.CUTOUT
            ChunkSectionLayer.CUTOUT_MIPPED -> BlockRenderSettings.CUTOUT
            ChunkSectionLayer.TRANSLUCENT -> BlockRenderSettings.TRANSLUCENT
            ChunkSectionLayer.TRIPWIRE -> BlockRenderSettings.TRIPWIRE
            //? } else {
            /*RenderType.solid() -> BlockRenderSettings.SOLID
            RenderType.cutout(), RenderType.cutoutMipped() -> BlockRenderSettings.CUTOUT
            RenderType.translucent() -> BlockRenderSettings.TRANSLUCENT
            RenderType.tripwire() -> BlockRenderSettings.TRIPWIRE
            else -> null
            *///? }
        }
    }

    @Suppress("DEPRECATION")
    override fun shouldRenderFace(
        level: BlockGetter,
        pos: NeoBlockPos,
        direction: NeoDirection,
        state: BlockState
    ): Boolean {
        //? if >=1.21.2 {
        return Block.shouldRenderFace(state, level.getBlockState((pos + direction).mojang), direction.mojang)
        //? } else {
        /*return Block.shouldRenderFace(state, level, (pos + direction).mojang, direction.mojang, pos.mojang)
        *///? }
    }
}