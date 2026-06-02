package net.typho.big_shot_lib.api.client.event

fun interface DisplayResizedEvent {
    fun onDisplayResized(
        windowWidth: Int,
        windowHeight: Int,
        framebufferWidth: Int,
        framebufferHeight: Int
    )
}