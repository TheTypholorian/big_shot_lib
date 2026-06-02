package net.typho.big_shot_lib.impl.client

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.event.RegisterMainMenuModesEvent
import net.typho.big_shot_lib.api.client.rendering.util.MainMenuMode
import net.typho.big_shot_lib.api.util.mutableLazy

object MainMenuModeManager {
    @JvmField
    val mainMenuModes = mutableListOf(MainMenuMode.MINECRAFT)

    @JvmStatic
    var selected: MainMenuMode by mutableLazy { mainMenuModes.first() }
        private set
    @JvmStatic
    @set:JvmName("setSelected")
    var selectedIndex: Int = 0
        set(value) {
            field = value % mainMenuModes.size
            selected = mainMenuModes[field]
        }
    @JvmStatic
    @set:JvmName("setSelected")
    var selectedId: Identifier
        get() = selected.id
        set(value) {
            mainMenuModes.firstOrNull { it.id == value }?.let { selected = it }
        }

    fun register(event: RegisterMainMenuModesEvent) {
        if (mainMenuModes.addAll(event.getMainMenuModes())) {
            mainMenuModes.sortWith(Comparator.comparingInt { -it.priority })
        }
    }
}