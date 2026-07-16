package net.typho.big_shot_lib.client.api.event

import net.minecraft.client.Minecraft

fun interface ClientEndFrameEvent {
    fun onClientEndFrame(
        client: Minecraft
    )
}