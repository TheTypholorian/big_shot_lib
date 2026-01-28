package net.typho.big_shot_lib.resource

import net.minecraft.core.Holder
import net.minecraft.core.HolderOwner
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey

class TagWithValues<T>(
    owner: HolderOwner<T?>,
    key: TagKey<T?>,
    values: MutableList<Holder<T?>>
) : HolderSet.Named<T>(owner, key) {
    init {
        bind(values)
    }
}