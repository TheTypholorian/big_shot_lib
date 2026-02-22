package net.typho.big_shot_lib.impl.registration

import net.minecraft.client.KeyMapping
import net.minecraft.network.chat.Component
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.registration.KeyMappingCategory

@JvmRecord
data class KeyMappingCategoryImpl(
    @JvmField
    val inner: KeyMapping.Category
) : KeyMappingCategory {
    override fun label(): Component {
        return inner.label()
    }

    override fun location() = inner.id.toNeo()
}