package net.typho.big_shot_lib.impl.client.util

//? if <1.20.2 {
import net.minecraft.client.gui.components.AccessibilityOnboardingTextWidget
//? } else if <1.20.5 {
/*import net.minecraft.client.gui.components.FocusableTextWidget
*///? } else {
/*import net.minecraft.client.gui.components.FocusableTextWidget
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
*///? }

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.LogoRenderer
import net.minecraft.client.gui.layouts.FrameLayout
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import sun.tools.jconsole.LabeledComponent.layout

open class InitialScreen(
    component: Component,
    @JvmField
    val onClose: () -> Unit,
    @JvmField
    val logo: LogoRenderer = LogoRenderer(true)
) : Screen(component) {
    //? if <1.20.2 {
    protected var textWidget: AccessibilityOnboardingTextWidget? = null
    //? } else if <1.20.5 {
    /*protected var textWidget: FocusableTextWidget? = null
    *///? } else {
    /*//? if <1.21.11 {
    protected var textWidget: FocusableTextWidget? = null
    //? }
    protected val layout = HeaderAndFooterLayout(this, 90, 33)
    *///? }

    override fun init() {
        //? if <1.20.5 {
        val frameLayout = FrameLayout(width, height - 90)
        frameLayout.defaultChildLayoutSetting().alignVerticallyTop().padding(4)
        val gridLayout = frameLayout.addChild(GridLayout())
        gridLayout.defaultCellSetting().alignHorizontallyCenter().padding(4)
        val row = gridLayout.createRowHelper(1)
        row.defaultCellSetting().padding(2)

        //? if <1.20.2 {
        textWidget = AccessibilityOnboardingTextWidget(font, title, width)
        //? } else {
        /*textWidget = FocusableTextWidget(width - 16, title, font)
        *///? }

        row.addChild(textWidget!!, row.newCellSettings().paddingBottom(16))
        frameLayout.addChild(Button.builder(CommonComponents.GUI_CONTINUE) { onClose() }.build(), frameLayout.newChildLayoutSettings().alignVerticallyBottom().padding(8))
        frameLayout.arrangeElements()
        FrameLayout.alignInRectangle(frameLayout, 0, 90, width, height, 0.5f, 0f)
        frameLayout.visitWidgets { addRenderableWidget(it) }
        //? } else {
        /*val layout = layout.addToContents(LinearLayout.vertical())
        layout.defaultCellSetting().alignHorizontallyCenter().padding(4)
        //? if <1.21.11 {
        textWidget = layout.addChild(FocusableTextWidget(width, title, font)) { it.padding(8) }
        //? } else {
        /*layout.addChild(FocusableTextWidget.builder(title, font).maxWidth(374).build(), { it.padding(8) })
        *///? }
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_CONTINUE) { onClose() }.build())
        this.layout.visitWidgets { addRenderableWidget(it) }
        repositionElements()
        *///? }
    }

    //? if >=1.20.5 {
    /*override fun repositionElements() {
        //? if <1.21.11 {
        textWidget?.containWithin(width)
        //? }
        layout.arrangeElements()
    }
    *///? }

    override fun onClose() {
        onClose.invoke()
    }

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        super.render(guiGraphics, i, j, f)
        logo.renderLogo(guiGraphics, width, 1f)
    }
}