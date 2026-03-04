package net.typho.big_shot_lib.api.client.util

import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.BigShotApi.loadService

interface BlockRenderSettingsUtil {
    fun getBlockSettings(state: BlockState): BlockRenderSettings

    companion object {
        @JvmField
        val INSTANCE = BlockRenderSettingsUtil::class.loadService()
    }
}