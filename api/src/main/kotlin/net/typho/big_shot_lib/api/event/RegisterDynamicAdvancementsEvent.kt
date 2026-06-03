package net.typho.big_shot_lib.api.event

import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup

fun interface RegisterDynamicAdvancementsEvent {
    fun register(output: Output, registries: HolderLookup.Provider)

    interface Output {
        fun register(advancement: AdvancementHolder)
    }
}