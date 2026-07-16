package net.typho.big_shot_lib.client.api.event

fun interface RegisterDebugScreenEntriesEvent {
    fun registerDebugScreenEntry(output: (entry: DebugScreenEntry) -> Unit)
}