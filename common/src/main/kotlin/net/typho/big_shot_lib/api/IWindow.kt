package net.typho.big_shot_lib.api

import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import org.lwjgl.glfw.GLFW.*

interface IWindow {
    fun handle(): Long

    fun width(): Int

    fun height(): Int

    fun guiWidth(): Int = (width() / Minecraft.getInstance().window.guiScale).toInt()

    fun guiHeight(): Int = (height() / Minecraft.getInstance().window.guiScale).toInt()

    fun framebuffer(): IFramebuffer

    fun createGraphics(): GuiGraphics

    fun render(deltaTracker: DeltaTracker)

    companion object {
        @JvmStatic
        fun create(
            width: Int,
            height: Int,
            title: String
        ): Long {
            glfwDefaultWindowHints()
            glfwWindowHint(139265, 196609)
            glfwWindowHint(139275, 221185)
            glfwWindowHint(139266, 3)
            glfwWindowHint(139267, 2)
            glfwWindowHint(139272, 204801)
            glfwWindowHint(139270, 1)
            return glfwCreateWindow(width, height, title, 0L, Minecraft.getInstance().window.window)
        }
    }
}