package net.typho.big_shot_lib.api.util

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface RegistrationConsumer<T : Any, I> {
    fun <V : T> register(id: I, value: () -> V): RegisteredObject<V>

    companion object {
        @JvmStatic
        fun <V : NamedResource> RegistrationConsumer<V, NeoIdentifier>.register(value: V): RegisteredObject<V> {
            return register(value.location) { value }
        }
    }

    interface Blocks<I> {
        fun <V : Block> register(id: I, value: (properties: BlockBehaviour.Properties) -> V): RegisteredObject<V>
    }

    interface Items<I> {
        fun <V : Item> register(id: I, value: (properties: Item.Properties) -> V): RegisteredObject<V>

        fun registerBlockItem(id: I, block: () -> Block, properties: (properties: Item.Properties) -> Item.Properties): RegisteredObject<BlockItem>
    }
}