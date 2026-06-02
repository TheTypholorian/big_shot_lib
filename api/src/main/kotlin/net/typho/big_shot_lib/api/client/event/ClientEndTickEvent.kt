package net.typho.big_shot_lib.api.client.event

import net.minecraft.client.Minecraft

fun interface ClientEndTickEvent {
    fun onClientEndTick(
        client: Minecraft
    )
}