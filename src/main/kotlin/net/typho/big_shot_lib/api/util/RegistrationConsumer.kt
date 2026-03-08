package net.typho.big_shot_lib.api.util

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface RegistrationConsumer<T : Any> {
    fun <V : T> register(id: String, value: () -> V): RegisteredObject<V>

    fun <V : T> register(id: ResourceIdentifier, value: () -> V): RegisteredObject<V>

    companion object {
        @JvmStatic
        fun <V : NamedResource> RegistrationConsumer<V>.register(value: V): RegisteredObject<V> {
            return register(value.location) { value }
        }
    }

    interface Blocks {
        fun <V : Block> register(id: String, value: (properties: BlockBehaviour.Properties) -> V): RegisteredObject<V>
    }

    interface Items {
        fun <V : Item> register(id: String, value: (properties: Item.Properties) -> V): RegisteredObject<V>

        fun registerBlockItem(id: String, block: () -> Block, properties: (properties: Item.Properties) -> Item.Properties): RegisteredObject<BlockItem>
    }
}