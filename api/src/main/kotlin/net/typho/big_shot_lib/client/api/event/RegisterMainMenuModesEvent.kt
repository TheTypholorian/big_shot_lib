package net.typho.big_shot_lib.client.api.event

import net.typho.big_shot_lib.client.api.rendering.util.MainMenuMode

fun interface RegisterMainMenuModesEvent {
    fun getMainMenuModes(): Iterable<MainMenuMode>
}