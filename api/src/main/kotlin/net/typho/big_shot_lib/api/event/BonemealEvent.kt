package net.typho.big_shot_lib.api.event

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

fun interface BonemealEvent {
    fun bonemeal(
        level: Level,
        player: Player?,
        pos: BlockPos,
        state: BlockState,
        stack: ItemStack,
        isValidBonemealTarget: Boolean
    ): Boolean
}