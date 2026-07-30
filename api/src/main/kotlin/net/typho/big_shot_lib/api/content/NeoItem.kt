package net.typho.big_shot_lib.api.content

import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.JukeboxPlayable
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level

open class NeoItem(
    /**
     * Exposed via [NeoItem.asItem]
     */
    private val item: Item
) : ItemLike {
    override fun asItem(): Item {
        return item
    }

    open fun onUseTick(
        level: Level,
        user: LivingEntity,
        stack: ItemStack,
        remainingUseTicks: Int
    ) {
    }

    interface Properties {
        // TODO food

        fun usingConvertsTo(item: ItemLike): Properties

        fun useCooldown(ticks: Int): Properties

        fun stacksTo(max: Int): Properties

        fun craftRemainder(item: ItemLike): Properties

        fun rarity(rarity: Rarity): Properties

        fun fireResistant(): Properties

        fun jukeboxPlayable(song: ResourceKey<JukeboxPlayable>): Properties

        fun enchantable(value: Int): Properties

        fun repairable(item: ItemLike): Properties

        fun repairable(items: TagKey<Item>): Properties

        fun equippable(slot: EquipmentSlot): Properties

        fun equippableUnswappable(slot: EquipmentSlot): Properties

        // TODO tools

        fun spawnEgg(type: EntityType<*>): Properties

        // TODO armor
        // TODO trims
        // TODO feature flags
        // TODO components
        // TODO attributes
    }
}