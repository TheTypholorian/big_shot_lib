package net.typho.big_shot_lib.client.api

import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

private val INSTANCE by lazy { IInternalClientUtil::class.loadService() }

object InternalClientUtil : IInternalClientUtil by INSTANCE

interface IInternalClientUtil {
    fun getEventBus(modId: String): NeoClientEventBus
}