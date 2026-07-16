package net.typho.big_shot_lib.impl

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
import net.typho.big_shot_lib.api.IInternalUtil
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegistryBuilder

object InternalUtilImpl : IInternalUtil {
    override fun <T : Any> createRegistryBuilder(key: ResourceKey<Registry<T>>): RegistryBuilder<T> {
        return RegistryBuilderImpl(key)
    }

    override fun createCreativeTabBuilder(): CreativeModeTab.Builder {
        TODO("")
        //return CreativeModeTab.builder()
    }

    override fun createStairBlock(
        parentState: BlockState,
        properties: BlockBehaviour.Properties
    ): StairBlock {
        return StairBlock(parentState, properties)
    }

    override fun createDoorBlock(
        blockSet: BlockSetType,
        properties: BlockBehaviour.Properties
    ): DoorBlock {
        return DoorBlock(blockSet, properties)
    }

    override fun createTrapDoorBlock(
        blockSet: BlockSetType,
        properties: BlockBehaviour.Properties
    ): TrapDoorBlock {
        return TrapDoorBlock(blockSet, properties)
    }

    override fun createPressurePlateBlock(
        blockSet: BlockSetType,
        properties: BlockBehaviour.Properties
    ): PressurePlateBlock {
        return PressurePlateBlock(blockSet, properties)
    }

    override fun createButtonBlock(
        blockSet: BlockSetType,
        pressDuration: Int,
        properties: BlockBehaviour.Properties
    ): ButtonBlock {
        return ButtonBlock(blockSet, pressDuration, properties)
    }

    override fun getEventBus(modId: String): NeoEventBus {
        return NeoEventBusImpl
    }
}