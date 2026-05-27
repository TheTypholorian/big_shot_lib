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
        @JvmField
        val VIBRANCY_1 = MainMenuMode(
            priority = 4000,
            id = BigShotApi.id("vibrancy_1"),
            panorama = CubeMap(BigShotApi.id("textures/gui/title/background/trial_chamber")),
            editionHoverLightColor = NeoColor.RGB(222, 177, 45),
            editionHoverDarkColor = NeoColor.RGB(177, 103, 18)
        )
        @JvmField
        val VIBRANCY_2 = MainMenuMode(
            priority = 3000,
            id = BigShotApi.id("vibrancy_2"),
            panorama = CubeMap(BigShotApi.id("textures/gui/title/background/lush_cave")),
            editionHoverLightColor = NeoColor.RGB(247, 226, 107),
            editionHoverDarkColor = NeoColor.RGB(244, 192, 94)
        )
        @JvmField
        val VIBRANCY_3 = MainMenuMode(
            priority = 2000,
            id = BigShotApi.id("vibrancy_3"),
            panorama = CubeMap(BigShotApi.id("textures/gui/title/background/ancient_city")),
            editionHoverLightColor = NeoColor.RGB(122, 245, 248),
            editionHoverDarkColor = NeoColor.RGB(42, 201, 207)
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