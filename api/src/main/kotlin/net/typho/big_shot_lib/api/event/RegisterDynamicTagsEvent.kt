package net.typho.big_shot_lib.api.event

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.Consumer

fun interface RegisterDynamicTagsEvent {
    fun register(output: Output)

    interface Output {
        fun <T : Any> add(registry: ResourceKey<Registry<T>>, tag: TagKey<out T>, vararg entries: ResourceKey<out T>)

        fun addItems(tag: TagKey<out Item>, vararg entries: ResourceKey<out Item>) = add(Registries.ITEM, tag, *entries)

        fun addBlocks(tag: TagKey<out Block>, vararg entries: ResourceKey<out Block>) = add(Registries.BLOCK, tag, *entries)
    }
}