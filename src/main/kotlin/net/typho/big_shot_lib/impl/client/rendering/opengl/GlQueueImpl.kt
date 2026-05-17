package net.typho.big_shot_lib.impl.client.rendering.opengl

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue

object GlQueueImpl : GlQueue {
    @JvmField
    val queue = arrayListOf<Runnable>()

    override fun queue(task: Runnable) {
        synchronized(queue) {
            queue.add(task)
        }
    }

    override fun runOrQueue(task: Runnable) {
        if (RenderSystem.isOnRenderThread()) {
            task.run()
        } else {
            queue(task)
        }
    }
}