package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.blaze3d.platform.GlDebug
import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_RESIZABLE
import org.lwjgl.glfw.GLFW.GLFW_VISIBLE
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDefaultWindowHints
import org.lwjgl.glfw.GLFW.glfwDestroyWindow
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil.NULL
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.collections.set

object GlThreadPool : ThreadPoolExecutor(4, 4, 10L, TimeUnit.MINUTES, LinkedBlockingQueue(), ThreadFactory { task ->
    val main = Minecraft.getInstance().window.window
    glfwMakeContextCurrent(main)

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

    val handle = glfwCreateWindow(1, 1, "Big Shot Lib GlThreadPool context", NULL, main)

    if (handle == NULL) {
        throw NullPointerException("Couldn't create GlThreadPool window")
    }

    glfwMakeContextCurrent(handle)
    val cap = GL.createCapabilities()
    GlDebug.enableDebugCallback(1, true)

    val thread = object : Thread() {
        override fun run() {
            val thread = this

            glfwMakeContextCurrent(handle)
            GL.setCapabilities(cap)

            val queue = arrayListOf<() -> Unit>()

            RenderingContext.BY_HANDLE[handle] = object : RenderingContext {
                override val handle: Long = handle
                override val name: String = "GlThreadPool"
                override val glManager: NeoGlStateManager = NeoGlStateManager.Standalone()

                override fun isOnMainThread(): Boolean {
                    return currentThread() == thread
                }

                override fun queue(task: () -> Unit) {
                    synchronized(queue) {
                        queue.add(task)
                    }
                }

                override fun runOrQueue(task: () -> Unit) {
                    if (isOnMainThread()) {
                        task()
                    } else {
                        queue(task)
                    }
                }
            }

            try {
                task.run()
            } finally {
                queue.forEach { it() }
                glfwMakeContextCurrent(NULL)
                GL.setCapabilities(null)
                glfwDestroyWindow(handle)
                RenderingContext.BY_HANDLE.remove(handle)
            }
        }
    }

    glfwMakeContextCurrent(main)

    return@ThreadFactory thread
})