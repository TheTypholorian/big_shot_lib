package net.typho.big_shot_lib.api.client.util

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface KeyMappingFactory {
    val movement: KeyMappingCategory
        get() = getOrCreateCategory(ResourceIdentifier("movement"))
    val gameplay: KeyMappingCategory
        get() = getOrCreateCategory(ResourceIdentifier("gameplay")) 
    val inventory : KeyMappingCategory
        get() = getOrCreateCategory(ResourceIdentifier("inventory")) 
    val multiplayer : KeyMappingCategory
        get() = getOrCreateCategory(ResourceIdentifier("multiplayer")) 
    val ui : KeyMappingCategory
        get() = getOrCreateCategory(ResourceIdentifier("ui")) 
    val creative : KeyMappingCategory
        get() = getOrCreateCategory(ResourceIdentifier("creative")) 
    val spectator : KeyMappingCategory
        get() = getOrCreateCategory(ResourceIdentifier("spectator")) 
    val misc : KeyMappingCategory
        get() = getOrCreateCategory(ResourceIdentifier("misc")) 
    val debug : KeyMappingCategory
        get() = getOrCreateCategory(ResourceIdentifier("debug"))
    
    fun getOrCreateCategory(id: ResourceIdentifier): KeyMappingCategory

    fun create(id: ResourceIdentifier, key: Int, category: KeyMappingCategory): KeyMapping

    fun create(id: ResourceIdentifier, type: InputConstants.Type, key: Int, category: KeyMappingCategory): KeyMapping
}