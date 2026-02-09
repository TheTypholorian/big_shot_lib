package net.typho.big_shot_lib.api.event

import java.util.*

fun interface PostProcessEvent {
    fun invoke(data: RenderData)

    companion object : PostProcessEvent {
        private val callbacks = LinkedList<PostProcessEvent>()

        @JvmStatic
        fun register(callback: PostProcessEvent) = callbacks.add(callback)

        override fun invoke(data: RenderData) {
            callbacks.forEach { it.invoke(data) }
        }
    }
}