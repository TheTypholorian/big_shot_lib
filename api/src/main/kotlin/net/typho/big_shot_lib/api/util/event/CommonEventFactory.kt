package net.typho.big_shot_lib.api.util.event

interface CommonEventFactory {
    val blockChanged: MutableList<BlockChangedEvent>
}