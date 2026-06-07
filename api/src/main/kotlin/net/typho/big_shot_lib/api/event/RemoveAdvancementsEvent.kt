package net.typho.big_shot_lib.api.event

import net.minecraft.advancements.AdvancementHolder

fun interface RemoveAdvancementsEvent {
    fun shouldRemove(advancement: AdvancementHolder): Boolean
}