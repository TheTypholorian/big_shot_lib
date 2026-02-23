package net.typho.big_shot_lib.api.util

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

interface RegistrationConsumer<T : Any> {
    fun <V : T> register(id: String, value: Supplier<V>): RegisteredObject<V>

    interface Blocks {
        fun <V : Block> register(id: String, value: Function<BlockBehaviour.Properties, V>): RegisteredObject<V>
    }

    interface Items {
        fun <V : Item> register(id: String, value: Function<Item.Properties, V>): RegisteredObject<V>

        fun registerBlockItem(id: String, block: Supplier<out Block>, properties: UnaryOperator<Item.Properties>): RegisteredObject<BlockItem>
    }
}