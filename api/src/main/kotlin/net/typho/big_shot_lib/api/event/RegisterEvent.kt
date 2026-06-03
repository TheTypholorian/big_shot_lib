package net.typho.big_shot_lib.api.event

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.typho.big_shot_lib.api.util.content.RegisteredObject
import java.util.function.Consumer

fun interface RegisterEvent {
    fun register(output: Output)

    interface Output {
        fun <T : Any> begin(key: Identifier, out: Consumer<RegistrationConsumer<T>>)

        fun <T : Any> begin(key: ResourceKey<Registry<T>>, out: Consumer<RegistrationConsumer<T>>)

        fun <T : Any> begin(registry: Registry<T>, out: Consumer<RegistrationConsumer<T>>)

        fun beginBlocks(out: Consumer<RegistrationConsumer<Block>>) = begin(BuiltInRegistries.BLOCK, out)

        fun beginItems(out: Consumer<RegistrationConsumer<Item>>) = begin(BuiltInRegistries.ITEM, out)
    }

    interface RegistrationConsumer<T : Any> {
        fun register(key: Identifier, value: T)

        fun register(key: ResourceKey<T>, value: T)

        fun <V : T> register(obj: RegisteredObject<V>)
    }
}