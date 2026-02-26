package net.typho.big_shot_lib.impl.registration

import net.minecraft.network.chat.Component
import net.typho.big_shot_lib.api.client.util.KeyMappingCategory
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

@JvmRecord
data class KeyMappingCategoryImpl(
    override val location: ResourceIdentifier,
    @JvmField
    val label: String = "key.category.${location.toShortLanguageKey()}"
) : KeyMappingCategory {
    override fun label(): Component {
        return Component.translatable(label)
    }
}