package net.typho.big_shot_lib.api.client.util.event

interface ClientEventFactory {
    val clientTickStart: MutableList<Runnable>
    val clientTickEnd: MutableList<Runnable>
    val levelRenderEnd: MutableList<RenderEvent>
    val levelChanged: MutableList<ClientLevelChangedEvent>
}