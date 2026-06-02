package net.typho.big_shot_lib.api.client.event

fun interface RegisterDebugScreenEntriesEvent {
    fun registerDebugScreenInfo(info: DebugScreenEntry)
}