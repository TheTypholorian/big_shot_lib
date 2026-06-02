package net.typho.big_shot_lib.api.client.event

import net.minecraft.client.Minecraft

fun interface ClientStartTickEvent {
    fun onClientStartTick(
        client: Minecraft
    )
}