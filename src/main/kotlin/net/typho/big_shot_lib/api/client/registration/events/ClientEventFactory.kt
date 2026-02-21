package net.typho.big_shot_lib.api.client.registration.events

interface ClientEventFactory {
    fun onFrameStart(event: Runnable)

    fun onLevelRenderEnd(event: RenderEvent)

    fun onFrameEnd(event: Runnable)

    fun onWindowResized(event: WindowResizeEvent)
}