package net.typho.big_shot_lib.api.event

import net.minecraft.server.MinecraftServer

fun interface ServerStartTickEvent {
    fun serverStartTick(
        server: MinecraftServer
    )
}