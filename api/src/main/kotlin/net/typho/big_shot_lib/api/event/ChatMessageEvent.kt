package net.typho.big_shot_lib.api.event

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

fun interface ChatMessageEvent {
    fun onChatMessage(
        player: Player,
        username: String,
        rawText: String,
        message: Component
    ): Component
}