package net.typho.big_shot_lib

import net.typho.big_shot_lib.api.client.registration.BigShotClientRegistrationEntrypoint
import net.typho.big_shot_lib.api.client.registration.events.ClientEventFactory
import net.typho.big_shot_lib.api.client.registration.events.RenderEvent
import net.typho.big_shot_lib.api.client.registration.events.WindowResizeEvent
import java.util.*

object BigShotClientEventStorage : ClientEventFactory {
    @JvmField
    val onFrameStart = LinkedList<Runnable>()
    @JvmField
    val onLevelRenderEnd = LinkedList<RenderEvent>()
    @JvmField
    val onFrameEnd = LinkedList<Runnable>()
    @JvmField
    val onWindowResized = LinkedList<WindowResizeEvent>()

    init {
        BigShotClientRegistrationEntrypoint.registerEvents(this)
    }

    override fun onFrameStart(event: Runnable) {
        onFrameStart.add(event)
    }

    override fun onLevelRenderEnd(event: RenderEvent) {
        onLevelRenderEnd.add(event)
    }

    override fun onFrameEnd(event: Runnable) {
        onFrameEnd.add(event)
    }

    override fun onWindowResized(event: WindowResizeEvent) {
        onWindowResized.add(event)
    }
}