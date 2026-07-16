package net.typho.big_shot_lib.client.api.event

import net.minecraft.network.chat.Component

fun interface InitialScreenEvent {
    fun displayInitialScreens(
        out: (text: Component, onClose: () -> Unit) -> Unit
    )
}