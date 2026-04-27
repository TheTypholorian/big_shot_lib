package net.typho.big_shot_lib.api.util

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
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
import net.typho.big_shot_lib.api.client.util.Registrar
import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

abstract class BigShotCommonEntrypoint : ModEntrypoint(), Registrar {
    private val registrar = PlatformUtil.INSTANCE.createRegistrar()

    abstract fun onInitialize()

    override fun <T : Any> register(registry: NeoRegistry<out Registry<T>>, id: NeoIdentifier, value: T): RegisteredObject<T> {
        return registrar.register(registry, id, value)
    }

    open fun onBlockChanged(
        level: Level,
        pos: BlockPos,
        old: BlockState,
        new: BlockState
    ) {
    }

    open fun chunkLoaded(
        level: LevelAccessor,
        chunk: ChunkAccess
    ) {
    }

    open fun chunkUnloaded(
        level: LevelAccessor,
        chunk: ChunkAccess
    ) {
    }

    open fun useItemOnBlock(
        level: Level,
        player: Player?,
        hand: InteractionHand,
        item: ItemStack,
        pos: BlockPos,
        face: NeoDirection?,
        context: UseOnContext
    ): ItemInteractionResult {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    open fun bonemeal(
        level: Level,
        player: Player?,
        pos: BlockPos,
        state: BlockState,
        stack: ItemStack,
        isValidBonemealTarget: Boolean
    ): Boolean {
        return false
    }

    open fun chatMessage(
        player: Player,
        username: String,
        rawText: String,
        message: Component
    ): Component {
        return message
    }

    open fun commonCommands(
        dispatcher: CommandDispatcher<CommandSourceStack>,
        environment: Commands.CommandSelection,
        context: CommandBuildContext
    ) {
    }

    open fun serverTick(
        hasTime: () -> Boolean,
        server: MinecraftServer
    ) {
    }

    companion object {
        val entrypoints by lazy { BigShotCommonEntrypoint::class.loadServices() }
    }
}