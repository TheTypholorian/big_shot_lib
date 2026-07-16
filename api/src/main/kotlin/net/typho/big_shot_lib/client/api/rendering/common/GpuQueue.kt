package net.typho.big_shot_lib.client.api.rendering.common

import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

private val INSTANCE by lazy { IGpuQueue::class.loadService() }

object GpuQueue : IGpuQueue by INSTANCE

interface IGpuQueue {
    /**
     * Queue the task to be run at the end of the frame
     */
    fun queue(task: Runnable)

    /**
     * Run the task immediately if on the render thread, otherwise queue it
     */
    fun runOrQueue(task: Runnable)
}