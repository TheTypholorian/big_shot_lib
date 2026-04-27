package net.typho.big_shot_lib.api.util

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoTagKey

interface NeoRegistry<T : Any> {
    val key: NeoResourceKey<out Registry<T>>

    fun get(value: NeoIdentifier): T?

    fun get(value: NeoResourceKey<T>) = get(value.location)

    fun getKey(value: T): NeoResourceKey<T>

    fun contains(value: NeoIdentifier): Boolean

    fun contains(value: NeoResourceKey<T>) = contains(value.location)

    fun keys(): Set<NeoResourceKey<T>>

    fun entries(): Set<Pair<NeoResourceKey<T>, T>>

    fun values(): Collection<T>

    fun getTag(key: NeoTagKey<T>): Set<T>?

    fun tags(): Set<NeoTagKey<T>>

    fun addAlias(
        old: NeoIdentifier,
        new: NeoIdentifier
    )

    companion object {
        val GAME_EVENT = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.GAME_EVENT)
        val SOUND_EVENT = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.SOUND_EVENT)
        val FLUID = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.FLUID)
        val MOB_EFFECT = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.MOB_EFFECT)
        val BLOCK = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.BLOCK)
        val ENTITY_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ENTITY_TYPE)
        val ITEM = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ITEM)
        val POTION = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.POTION)
        val PARTICLE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.PARTICLE_TYPE)
        val BLOCK_ENTITY_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.BLOCK_ENTITY_TYPE)
        val CUSTOM_STAT = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.CUSTOM_STAT)
        val CHUNK_STATUS = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.CHUNK_STATUS)
        val RULE_TEST = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.RULE_TEST)
        val RULE_BLOCK_ENTITY_MODIFIER = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER)
        val POS_RULE_TEST = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.POS_RULE_TEST)
        val MENU = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.MENU)
        val RECIPE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.RECIPE_TYPE)
        val RECIPE_SERIALIZER = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.RECIPE_SERIALIZER)
        val ATTRIBUTE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ATTRIBUTE)
        val POSITION_SOURCE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.POSITION_SOURCE_TYPE)
        val COMMAND_ARGUMENT_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.COMMAND_ARGUMENT_TYPE)
        val STAT_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.STAT_TYPE)
        val VILLAGER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.VILLAGER_TYPE)
        val VILLAGER_PROFESSION = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.VILLAGER_PROFESSION)
        val POINT_OF_INTEREST_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.POINT_OF_INTEREST_TYPE)
        val MEMORY_MODULE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.MEMORY_MODULE_TYPE)
        val SENSOR_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.SENSOR_TYPE)
        val SCHEDULE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.SCHEDULE)
        val ACTIVITY = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ACTIVITY)
        val LOOT_POOL_ENTRY_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE)
        val LOOT_FUNCTION_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.LOOT_FUNCTION_TYPE)
        val LOOT_CONDITION_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.LOOT_CONDITION_TYPE)
        val LOOT_NUMBER_PROVIDER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE)
        val LOOT_NBT_PROVIDER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE)
        val LOOT_SCORE_PROVIDER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE)
        val FLOAT_PROVIDER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.FLOAT_PROVIDER_TYPE)
        val INT_PROVIDER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.INT_PROVIDER_TYPE)
        val HEIGHT_PROVIDER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.HEIGHT_PROVIDER_TYPE)
        val BLOCK_PREDICATE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.BLOCK_PREDICATE_TYPE)
        val CARVER = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.CARVER)
        val FEATURE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.FEATURE)
        val STRUCTURE_PLACEMENT = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.STRUCTURE_PLACEMENT)
        val STRUCTURE_PIECE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.STRUCTURE_PIECE)
        val STRUCTURE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.STRUCTURE_TYPE)
        val PLACEMENT_MODIFIER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE)
        val BLOCKSTATE_PROVIDER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE)
        val FOLIAGE_PLACER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.FOLIAGE_PLACER_TYPE)
        val TRUNK_PLACER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.TRUNK_PLACER_TYPE)
        val ROOT_PLACER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ROOT_PLACER_TYPE)
        val TREE_DECORATOR_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.TREE_DECORATOR_TYPE)
        val FEATURE_SIZE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.FEATURE_SIZE_TYPE)
        val BIOME_SOURCE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.BIOME_SOURCE)
        val CHUNK_GENERATOR = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.CHUNK_GENERATOR)
        val MATERIAL_CONDITION = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.MATERIAL_CONDITION)
        val MATERIAL_RULE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.MATERIAL_RULE)
        val DENSITY_FUNCTION_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.DENSITY_FUNCTION_TYPE)
        val BLOCK_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.BLOCK_TYPE)
        val STRUCTURE_PROCESSOR = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.STRUCTURE_PROCESSOR)
        val STRUCTURE_POOL_ELEMENT = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.STRUCTURE_POOL_ELEMENT)
        val POOL_ALIAS_BINDING_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.POOL_ALIAS_BINDING_TYPE)
        val CAT_VARIANT = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.CAT_VARIANT)
        val FROG_VARIANT = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.FROG_VARIANT)
        val INSTRUMENT = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.INSTRUMENT)
        val DECORATED_POT_PATTERN = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.DECORATED_POT_PATTERN)
        val CREATIVE_MODE_TAB = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.CREATIVE_MODE_TAB)
        val TRIGGER_TYPES = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.TRIGGER_TYPES)
        val NUMBER_FORMAT_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.NUMBER_FORMAT_TYPE)
        val ARMOR_MATERIAL = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ARMOR_MATERIAL)
        val DATA_COMPONENT_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.DATA_COMPONENT_TYPE)
        val ENTITY_SUB_PREDICATE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE)
        val ITEM_SUB_PREDICATE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE)
        val MAP_DECORATION_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.MAP_DECORATION_TYPE)
        val ENCHANTMENT_EFFECT_COMPONENT_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE)
        val ENCHANTMENT_LEVEL_BASED_VALUE_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE)
        val ENCHANTMENT_ENTITY_EFFECT_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE)
        val ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE)
        val ENCHANTMENT_VALUE_EFFECT_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ENCHANTMENT_VALUE_EFFECT_TYPE)
        val ENCHANTMENT_PROVIDER_TYPE = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.ENCHANTMENT_PROVIDER_TYPE)
        val REGISTRY = WrapperUtil.INSTANCE.wrap(BuiltInRegistries.REGISTRY)
    }
}