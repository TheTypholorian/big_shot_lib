package net.typho.big_shot_lib.client.api.event

import net.minecraft.client.multiplayer.ClientLevel

fun interface ClientLevelChangedEvent {
    fun onClientLevelChanged(
        old: ClientLevel?,
        new: ClientLevel?
    )
}