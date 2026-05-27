package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.CubeMap
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.util.NeoColor

// TODO expand with dynamic rendering
data class MainMenuMode(
    @JvmField
    val priority: Int,
    @JvmField
    val id: Identifier,
    @JvmField
    val panorama: CubeMap? = null,
    @JvmField
    val logoImage: Identifier? = null,
    @JvmField
    val editionImage: Identifier? = null,
    @JvmField
    val editionHoverLightColor: NeoColor,
    @JvmField
    val editionHoverDarkColor: NeoColor
) {
    companion object {
        @JvmField
        val MINECRAFT = MainMenuMode(
            priority = 1000,
            id = Identifier.minecraft("builtin"),
            editionHoverLightColor = NeoColor.WHITE,
            editionHoverDarkColor = NeoColor.WHITE
        )

        @JvmStatic
        var selected: MainMenuMode = BigShotClientEntrypoint.mainMenuModes.first()
            private set
        @JvmStatic
        @set:JvmName("setSelected")
        var selectedIndex: Int = 0
            set(value) {
                field = value % BigShotClientEntrypoint.mainMenuModes.size
                selected = BigShotClientEntrypoint.mainMenuModes[field]
            }
        @JvmStatic
        @set:JvmName("setSelected")
        var selectedId: Identifier
            get() = selected.id
            set(value) {
                BigShotClientEntrypoint.mainMenuModes.firstOrNull { it.id == value }?.let { selected = it }
            }
    }
}