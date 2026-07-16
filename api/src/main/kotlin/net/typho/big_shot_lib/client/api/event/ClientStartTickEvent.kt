package net.typho.big_shot_lib.client.api.event

import net.minecraft.client.Minecraft

fun interface ClientStartTickEvent {
    fun onClientStartTick(
        client: Minecraft
    )
}