package net.typho.big_shot_lib.api.client.util.event

interface ClientEventFactory {
    val frameStart: MutableList<Runnable>
    val frameEnd: MutableList<Runnable>
    val levelRenderEnd: MutableList<RenderEvent>
    val windowResized: MutableList<WindowResizeEvent>
    val levelChanged: MutableList<ClientLevelChangedEvent>
}