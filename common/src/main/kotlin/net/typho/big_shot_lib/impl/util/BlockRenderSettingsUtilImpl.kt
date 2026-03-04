package net.typho.big_shot_lib.impl.util

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.client.util.BlockRenderSettings
import net.typho.big_shot_lib.api.client.util.BlockRenderSettingsUtil

class BlockRenderSettingsUtilImpl : BlockRenderSettingsUtil {
    override fun getBlockSettings(state: BlockState): BlockRenderSettings {
        return when (ItemBlockRenderTypes.getChunkRenderType(state)) {
            RenderType.solid() -> BlockRenderSettings.SOLID
            RenderType.cutout(), RenderType.cutoutMipped() -> BlockRenderSettings.CUTOUT
            RenderType.translucent(), RenderType.translucentMovingBlock() -> BlockRenderSettings.TRANSLUCENT
            RenderType.tripwire() -> BlockRenderSettings.TRIPWIRE
            else -> BlockRenderSettings.SOLID
        }
    }
}