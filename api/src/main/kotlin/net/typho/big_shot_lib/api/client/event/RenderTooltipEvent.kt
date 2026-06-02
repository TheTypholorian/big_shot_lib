package net.typho.big_shot_lib.api.client.event

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.world.item.ItemStack

fun interface RenderTooltipEvent {
    fun renderTooltip(
        stack: ItemStack,
        graphics: GuiGraphics,
        x: Int,
        y: Int,
        font: Font,
        components: List<ClientTooltipComponent>
    )
}