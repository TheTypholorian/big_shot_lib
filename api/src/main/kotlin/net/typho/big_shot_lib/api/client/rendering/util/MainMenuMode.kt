package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.client.renderer.CubeMap
import net.minecraft.resources.Identifier
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
    }
}