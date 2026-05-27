package net.typho.big_shot_lib.api.client.rendering

import net.minecraft.client.gui.components.events.GuiEventListener
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.util.MainMenuMode
import net.typho.big_shot_lib.api.plugin.Namespace

@Namespace(BigShotApi.MOD_ID)
interface AdvancedLogoRenderer : GuiEventListener {
    var enabled: Boolean
}