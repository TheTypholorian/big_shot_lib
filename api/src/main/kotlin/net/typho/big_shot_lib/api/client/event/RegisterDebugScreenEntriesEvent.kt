package net.typho.big_shot_lib.api.client.event

fun interface RegisterDebugScreenEntriesEvent {
    fun registerDebugScreenEntry(output: (entry: DebugScreenEntry) -> Unit)
}