package net.typho.big_shot_lib.api.client.util

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface KeyMappingFactory {
    val movement: KeyMappingCategory
        get() = getOrCreateCategory(NeoIdentifier("movement"))
    val gameplay: KeyMappingCategory
        get() = getOrCreateCategory(NeoIdentifier("gameplay"))
    val inventory : KeyMappingCategory
        get() = getOrCreateCategory(NeoIdentifier("inventory"))
    val multiplayer : KeyMappingCategory
        get() = getOrCreateCategory(NeoIdentifier("multiplayer"))
    val ui : KeyMappingCategory
        get() = getOrCreateCategory(NeoIdentifier("ui"))
    val creative : KeyMappingCategory
        get() = getOrCreateCategory(NeoIdentifier("creative"))
    val spectator : KeyMappingCategory
        get() = getOrCreateCategory(NeoIdentifier("spectator"))
    val misc : KeyMappingCategory
        get() = getOrCreateCategory(NeoIdentifier("misc"))
    val debug : KeyMappingCategory
        get() = getOrCreateCategory(NeoIdentifier("debug"))
    
    fun getOrCreateCategory(location: NeoIdentifier): KeyMappingCategory

    fun create(location: NeoIdentifier, key: Int, category: KeyMappingCategory): KeyMapping

    fun create(location: NeoIdentifier, type: InputConstants.Type, key: Int, category: KeyMappingCategory): KeyMapping
}