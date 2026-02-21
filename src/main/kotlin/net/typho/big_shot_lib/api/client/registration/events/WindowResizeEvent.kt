package net.typho.big_shot_lib.api.client.registration.events

fun interface WindowResizeEvent {
    fun invoke(width: Int, height: Int)
}