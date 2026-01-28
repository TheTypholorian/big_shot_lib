package net.typho.big_shot_lib.resource

import net.minecraft.core.Holder
import net.minecraft.core.HolderOwner
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

class KeyValueHolderReference<T>(
    owner: HolderOwner<T>,
    key: ResourceKey<T>,
    value: T,
    tags: Set<TagKey<T>>
) : Holder.Reference<T>(null, owner, key, value) {
    init {
        bindTags(tags)
    }
}