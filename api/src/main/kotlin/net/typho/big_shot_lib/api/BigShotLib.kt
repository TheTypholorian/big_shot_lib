package net.typho.big_shot_lib.api

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.event.NeoEventBus
import org.slf4j.LoggerFactory

object BigShotLib : NeoCommonInitializer {
    const val MOD_ID = "big_shot_lib"
    override val modId: String = MOD_ID
    @JvmField
    val LOGGER = LoggerFactory.getLogger("Big Shot Lib")

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(modId, path)

    override fun onInitialize(bus: NeoEventBus) {
    }
}