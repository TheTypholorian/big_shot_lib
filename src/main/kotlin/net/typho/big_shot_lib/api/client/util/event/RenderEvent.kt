package net.typho.big_shot_lib.api.client.util.event

fun interface RenderEvent {
    fun invoke(data: RenderEventData)
}