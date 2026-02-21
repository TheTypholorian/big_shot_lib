package net.typho.big_shot_lib.api.client.registration

import net.minecraft.network.chat.Component
import net.typho.big_shot_lib.api.util.resources.Named

interface KeyMappingCategory : Named {
    fun label(): Component
}