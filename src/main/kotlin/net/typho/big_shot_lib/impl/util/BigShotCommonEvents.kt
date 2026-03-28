package net.typho.big_shot_lib.impl.util

import net.typho.big_shot_lib.api.util.BigShotCommonEntrypoint
import net.typho.big_shot_lib.api.util.event.BlockChangedEvent
import net.typho.big_shot_lib.api.util.event.CommonEventFactory

object BigShotCommonEvents : CommonEventFactory {
    override val blockChanged: MutableList<BlockChangedEvent> = arrayListOf()

    init {
        BigShotCommonEntrypoint.registerEvents(this)
    }

    @JvmStatic
    internal fun init() = Unit
}