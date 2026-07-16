package net.typho.big_shot_lib.api

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegistryBuilder
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

private val INSTANCE by lazy { IInternalUtil::class.loadService() }

object InternalUtil : IInternalUtil by INSTANCE

interface IInternalUtil {
    fun <T : Any> createRegistryBuilder(
        key: ResourceKey<Registry<T>>
    ): RegistryBuilder<T>

    fun createCreativeTabBuilder(): CreativeModeTab.Builder

    fun createStairBlock(parentState: BlockState, properties: BlockBehaviour.Properties): StairBlock

    fun createDoorBlock(blockSet: BlockSetType, properties: BlockBehaviour.Properties): DoorBlock

    fun createTrapDoorBlock(blockSet: BlockSetType, properties: BlockBehaviour.Properties): TrapDoorBlock

    fun createPressurePlateBlock(blockSet: BlockSetType, properties: BlockBehaviour.Properties): PressurePlateBlock

    fun createButtonBlock(blockSet: BlockSetType, pressDuration: Int, properties: BlockBehaviour.Properties): ButtonBlock

    fun getEventBus(modId: String): NeoEventBus
}