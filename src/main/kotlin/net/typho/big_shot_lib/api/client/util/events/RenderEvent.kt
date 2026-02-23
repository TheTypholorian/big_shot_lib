package net.typho.big_shot_lib.api.client.util.events

fun interface RenderEvent {
    fun invoke(data: RenderEventData)
}