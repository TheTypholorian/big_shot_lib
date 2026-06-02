package net.typho.big_shot_lib.api.client.event

import net.minecraft.network.chat.ChatType
import net.minecraft.network.chat.Component
import java.util.UUID

fun interface ClientChatMessageEvent {
    fun clientChatMessage(
        message: Component,
        type: ChatType.Bound?,
        sender: UUID
    )
}