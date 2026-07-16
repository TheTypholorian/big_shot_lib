package net.typho.big_shot_lib.client.api.rendering

import net.minecraft.client.gui.components.events.GuiEventListener

interface AdvancedLogoRenderer : GuiEventListener {
    var enabled: Boolean
}