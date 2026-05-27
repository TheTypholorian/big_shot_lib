package net.typho.big_shot_lib.api.util

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.util.platform.PlatformUtil

interface BigShotCommonEntrypoint {
    fun onInitialize() {
    }

    fun onBlockChanged(
        level: Level,
        pos: BlockPos,
        old: BlockState,
        new: BlockState
    ) {
    }

    fun chunkLoaded(
        level: LevelAccessor,
        chunk: ChunkAccess
    ) {
    }

    fun chunkUnloaded(
        level: LevelAccessor,
        chunk: ChunkAccess
    ) {
    }

    fun useItemOnBlock(
        level: Level,
        player: Player?,
        hand: InteractionHand,
        item: ItemStack,
        pos: BlockPos,
        face: Direction?,
        context: UseOnContext
    ): ItemInteractionResult {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    fun bonemeal(
        level: Level,
        player: Player?,
        pos: BlockPos,
        state: BlockState,
        stack: ItemStack,
        isValidBonemealTarget: Boolean
    ): Boolean {
        return false
    }

    fun chatMessage(
        player: Player,
        username: String,
        rawText: String,
        message: Component
    ): Component {
        return message
    }

    fun commonCommands(
        dispatcher: CommandDispatcher<CommandSourceStack>,
        environment: Commands.CommandSelection,
        context: CommandBuildContext
    ) {
    }

    fun serverTick(
        hasTime: () -> Boolean,
        server: MinecraftServer
    ) {
    }

    companion object {
        val entrypoints by lazy { BigShotCommonEntrypoint::class.loadServices() }
    }
}