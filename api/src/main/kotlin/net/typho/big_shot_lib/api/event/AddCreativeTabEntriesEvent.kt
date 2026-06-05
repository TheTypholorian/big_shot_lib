package net.typho.big_shot_lib.api.event

import net.minecraft.resources.ResourceKey
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.typho.big_shot_lib.api.util.resource.RegisteredResource
import java.util.function.Consumer

fun interface AddCreativeTabEntriesEvent {
    fun addEntries(out: Output)

    interface Output {
        fun begin(tab: CreativeModeTab, out: Consumer<EntryConsumer>)

        fun begin(tab: ResourceKey<CreativeModeTab>, out: Consumer<EntryConsumer>)

        fun begin(tab: RegisteredResource<CreativeModeTab>, out: Consumer<EntryConsumer>) = begin(tab.key, out)
    }

    interface EntryConsumer {
        val flags: FeatureFlagSet

        fun showOperatorItems(): Boolean

        fun addFirst(visibility: CreativeModeTab.TabVisibility, stack: ItemStack)

        fun addFirst(stack: ItemStack) = addFirst(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, stack)

        fun addLast(visibility: CreativeModeTab.TabVisibility, stack: ItemStack)

        fun addLast(stack: ItemStack) = addLast(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, stack)

        fun addBefore(visibility: CreativeModeTab.TabVisibility, before: ItemStack, vararg insert: ItemStack)

        fun addBefore(before: ItemStack, vararg insert: ItemStack) = addBefore(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, before, *insert)

        fun addAfter(visibility: CreativeModeTab.TabVisibility, after: ItemStack, vararg insert: ItemStack)

        fun addAfter(after: ItemStack, vararg insert: ItemStack) = addAfter(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, after, *insert)

        fun remove(visibility: CreativeModeTab.TabVisibility, stack: ItemStack)

        fun remove(stack: ItemStack) = remove(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, stack)
    }
}