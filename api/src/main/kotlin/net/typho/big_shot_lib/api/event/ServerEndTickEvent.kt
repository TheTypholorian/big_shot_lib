package net.typho.big_shot_lib.api.event

import net.minecraft.server.MinecraftServer

fun interface ServerEndTickEvent {
    fun serverEndTick(
        server: MinecraftServer
    )
}