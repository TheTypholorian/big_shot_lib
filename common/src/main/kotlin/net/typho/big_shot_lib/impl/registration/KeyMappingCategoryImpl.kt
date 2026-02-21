package net.typho.big_shot_lib.impl.registration

import net.minecraft.network.chat.Component
import net.typho.big_shot_lib.api.client.registration.KeyMappingCategory
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

@JvmRecord
data class KeyMappingCategoryImpl(
    @JvmField
    val id: ResourceIdentifier,
    @JvmField
    val label: String = "key.category.${id.toShortLanguageKey()}"
) : KeyMappingCategory {
    override fun label(): Component {
        return Component.translatable(label)
    }

    override fun location() = id
}