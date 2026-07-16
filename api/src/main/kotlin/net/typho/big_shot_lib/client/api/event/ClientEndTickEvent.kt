package net.typho.big_shot_lib.client.api.event

import net.minecraft.client.Minecraft

fun interface ClientEndTickEvent {
    fun onClientEndTick(
        client: Minecraft
    )
}