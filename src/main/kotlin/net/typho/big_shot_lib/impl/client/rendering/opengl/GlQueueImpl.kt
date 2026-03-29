package net.typho.big_shot_lib.impl.client.rendering.opengl

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.event.ClientEventFactory

object GlQueueImpl : GlQueue {
    @JvmField
    val queue = arrayListOf<() -> Unit>()

    override fun queue(task: () -> Unit) {
        synchronized(queue) {
            queue.add(task)
        }
    }

    override fun runOrQueue(task: () -> Unit) {
        if (RenderSystem.isOnRenderThread()) {
            task()
        } else {
            queue(task)
        }
    }
}