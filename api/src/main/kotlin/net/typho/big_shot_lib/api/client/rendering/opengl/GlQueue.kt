package net.typho.big_shot_lib.api.client.rendering.opengl

import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

interface GlQueue {
    /**
     * Queue the task to be run at the end of the frame
     */
    fun queue(task: Runnable)

    /**
     * Run the task immediately if on the render thread, otherwise queue it
     */
    fun runOrQueue(task: Runnable)

    companion object {
        @JvmStatic
        val INSTANCE by lazy { GlQueue::class.loadService() }
    }
}