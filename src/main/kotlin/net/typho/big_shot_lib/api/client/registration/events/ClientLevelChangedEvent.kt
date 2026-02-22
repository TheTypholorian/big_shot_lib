package net.typho.big_shot_lib.api.client.registration.events

import net.minecraft.world.level.Level

fun interface ClientLevelChangedEvent {
    fun invoke(old: Level?, new: Level?)
}