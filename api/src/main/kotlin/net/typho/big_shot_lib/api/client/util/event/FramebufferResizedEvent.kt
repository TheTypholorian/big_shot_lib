package net.typho.big_shot_lib.api.client.util.event

fun interface FramebufferResizedEvent {
    fun invoke(width: Int, height: Int)
}