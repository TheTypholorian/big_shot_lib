package net.typho.big_shot_lib.api.client.event

import net.typho.big_shot_lib.api.client.rendering.util.MainMenuMode

fun interface RegisterMainMenuModesEvent {
    fun getMainMenuModes(): Iterable<MainMenuMode>
}