package net.typho.big_shot_lib.api.client.util

import net.minecraft.resources.Identifier
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping

interface KeyMappingFactory {
    val movement: KeyMappingCategory
        get() = getOrCreateCategory(Identifier.withDefaultNamespace("movement"))
    val gameplay: KeyMappingCategory
        get() = getOrCreateCategory(Identifier.withDefaultNamespace("gameplay"))
    val inventory : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.withDefaultNamespace("inventory"))
    val multiplayer : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.withDefaultNamespace("multiplayer"))
    val ui : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.withDefaultNamespace("ui"))
    val creative : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.withDefaultNamespace("creative"))
    val spectator : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.withDefaultNamespace("spectator"))
    val misc : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.withDefaultNamespace("misc"))
    val debug : KeyMappingCategory
        get() = getOrCreateCategory(Identifier.withDefaultNamespace("debug"))
    
    fun getOrCreateCategory(location: Identifier): KeyMappingCategory

    fun create(location: Identifier, key: Int, category: KeyMappingCategory): KeyMapping

    fun create(location: Identifier, type: InputConstants.Type, key: Int, category: KeyMappingCategory): KeyMapping
}