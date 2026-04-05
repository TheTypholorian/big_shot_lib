package net.typho.big_shot_lib.impl.client.util

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.FocusableTextWidget
import net.minecraft.client.gui.components.LogoRenderer
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

open class InitialScreen(
    component: Component,
    @JvmField
    val onClose: () -> Unit,
    @JvmField
    val logo: LogoRenderer = LogoRenderer(true)
) : Screen(component) {
    protected var textWidget: FocusableTextWidget? = null
    protected val layout = HeaderAndFooterLayout(this, 90, 33)

    override fun init() {
        val layout = layout.addToContents(LinearLayout.vertical())
        layout.defaultCellSetting().alignHorizontallyCenter().padding(4)
        textWidget = layout.addChild(FocusableTextWidget(width, title, font)) { it.padding(8) }
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_CONTINUE) { onClose() }.build())
        this.layout.visitWidgets { addRenderableWidget(it) }
        repositionElements()
    }

    override fun repositionElements() {
        textWidget?.containWithin(width)
        layout.arrangeElements()
    }

    override fun onClose() {
        onClose.invoke()
    }

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        super.render(guiGraphics, i, j, f)
        logo.renderLogo(guiGraphics, width, 1f)
    }
}