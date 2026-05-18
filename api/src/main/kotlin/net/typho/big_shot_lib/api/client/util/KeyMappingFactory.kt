package net.typho.big_shot_lib.api.client.util

import net.minecraft.resources.Identifier
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping

interface KeyMappingFactory {
    val movement: KeyMappingCategory
        get() = getOrCreateCategory(Identifier.minecraft("movement"))
    val gameplay: KeyMappingCategory
        get() = getOrCreateCategory(Identifier.minecraft("gameplay"))
    val inventory : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.minecraft("inventory"))
    val multiplayer : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.minecraft("multiplayer"))
    val ui : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.minecraft("ui"))
    val creative : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.minecraft("creative"))
    val spectator : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.minecraft("spectator"))
    val misc : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.minecraft("misc"))
    val debug : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.minecraft("debug"))
    
    fun getOrCreateCategory(location: Identifier): KeyMappingCategory

    fun create(location: Identifier, key: Int, category: KeyMappingCategory): KeyMapping

    fun create(location: Identifier, type: InputConstants.Type, key: Int, category: KeyMappingCategory): KeyMapping
}