package net.typho.big_shot_lib.api.impl

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.MultiBufferSource
import net.typho.big_shot_lib.api.IWindow

class NeoGuiGraphics(
    val window: IWindow,
    minecraft: Minecraft,
    bufferSource: MultiBufferSource.BufferSource
) : GuiGraphics(minecraft, bufferSource) {
    override fun guiWidth() = window.guiWidth()

    override fun guiHeight() = window.guiHeight()
}