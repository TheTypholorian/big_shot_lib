package net.typho.big_shot_lib.api.client.util

import net.minecraft.network.chat.Component

interface InitialScreenFactory {
    fun display(text: Component, onClose: () -> Unit = {})
}