package net.typho.big_shot_lib.api.client.registration.events

import net.minecraft.client.multiplayer.ClientLevel

fun interface ClientLevelChangedEvent {
    fun invoke(old: ClientLevel?, new: ClientLevel?)
}