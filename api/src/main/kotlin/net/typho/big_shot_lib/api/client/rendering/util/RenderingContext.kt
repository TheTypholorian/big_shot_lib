package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import org.lwjgl.glfw.GLFW.glfwGetCurrentContext
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.opengl.GL

interface RenderingContext : GlQueue {
    val handle: Long
    val name: String
    val glManager: NeoGlStateManager

    fun makeCurrent() {
        glfwMakeContextCurrent(handle)
        GL.createCapabilities()
    }

    fun isOnMainThread(): Boolean

    companion object {
        @JvmField
        val BY_HANDLE = mutableMapOf<Long, RenderingContext>()
        @JvmField
        val MAIN = object : RenderingContext {
            override val handle: Long
                get() = InternalUtil.INSTANCE.mainWindowHandle()
            override val name: String = "Main"
            override val glManager: NeoGlStateManager = NeoGlStateManager.MAIN

            override fun isOnMainThread(): Boolean {
                return RenderSystem.isOnRenderThread()
            }

            override fun queue(task: () -> Unit) {
                GlQueue.INSTANCE.queue(task)
            }

            override fun runOrQueue(task: () -> Unit) {
                GlQueue.INSTANCE.runOrQueue(task)
            }
        }

        init {
            BY_HANDLE[MAIN.handle] = MAIN
        }

        @JvmStatic
        fun get(context: Long = glfwGetCurrentContext()) = BY_HANDLE[context] ?: throw NullPointerException("No current rendering context")
    }
}