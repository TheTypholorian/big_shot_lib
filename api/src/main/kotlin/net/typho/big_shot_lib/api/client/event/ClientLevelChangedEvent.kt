package net.typho.big_shot_lib.api.client.event

import net.minecraft.client.multiplayer.ClientLevel

fun interface ClientLevelChangedEvent {
    fun onClientLevelChanged(
        old: ClientLevel?,
        new: ClientLevel?
    )
}