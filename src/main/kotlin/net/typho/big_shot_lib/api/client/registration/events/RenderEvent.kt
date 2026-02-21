package net.typho.big_shot_lib.api.client.registration.events

fun interface RenderEvent {
    fun invoke(data: RenderEventData)
}