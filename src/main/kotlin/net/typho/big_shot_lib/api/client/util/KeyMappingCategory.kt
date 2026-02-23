package net.typho.big_shot_lib.api.client.util

import net.minecraft.network.chat.Component
import net.typho.big_shot_lib.api.util.resources.NamedResource

interface KeyMappingCategory : NamedResource {
    fun label(): Component
}