package net.typho.big_shot_lib.api.event

import java.util.*

fun interface WindowResizeEvent {
    fun invoke(width: Int, height: Int)

    companion object : WindowResizeEvent {
        private val callbacks = LinkedList<WindowResizeEvent>()

        fun register(callback: WindowResizeEvent) = callbacks.add(callback)

        override fun invoke(width: Int, height: Int) {
            callbacks.forEach { it.invoke(width, height) }
        }
    }
}