package net.typho.big_shot_lib.api.client.event

import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics

fun interface RenderGUIEvent {
    fun renderGui(
        graphics: GuiGraphics,
        partialTick: DeltaTracker
    )
}