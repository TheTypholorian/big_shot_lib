package net.typho.big_shot_lib.api.render_queue

import java.util.*

interface RenderQueue {
    fun register(stage: RenderStage, event: RenderEvent)

    companion object : RenderQueue {
        private val INSTANCE = ServiceLoader.load(RenderQueue::class.java).findFirst().orElseThrow()

        override fun register(stage: RenderStage, event: RenderEvent) = INSTANCE.register(stage, event)
    }
}