package net.typho.big_shot_lib.api.event

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

fun interface UseItemOnBlockEvent {
    fun useItemOnBlock(
        level: Level,
        player: Player?,
        hand: InteractionHand,
        item: ItemStack,
        pos: BlockPos,
        face: Direction?,
        context: UseOnContext
    ): ItemInteractionResult
}