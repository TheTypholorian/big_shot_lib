package net.typho.big_shot_lib.impl.neoforge

import net.neoforged.bus.EventBusErrorMessage
import net.neoforged.bus.api.BusBuilder
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.event.IModBusEvent
import net.typho.big_shot_lib.api.NeoCommonInitializer
import net.typho.big_shot_lib.api.util.platform.NeoModLoader
import net.typho.big_shot_lib.client.api.NeoClientInitializer
import net.typho.big_shot_lib.common.annotation.Environment
import net.typho.big_shot_lib.common.loading.LoadingConstants
import org.slf4j.LoggerFactory
import kotlin.jvm.java

class ModContainerImpl(
    @JvmField
    val modInfo: ModInfoImpl,
) : ModContainer(modInfo) {
    companion object {
        @JvmField
        val LOGGER = LoggerFactory.getLogger("Big Shot Loader")
    }

    @Suppress("UnstableApiUsage")
    private val bus = BusBuilder.builder()
        .setExceptionHandler { bus, event, listeners, index, error -> LOGGER.error(buildString { EventBusErrorMessage(event, index, listeners, error).formatTo(this) }) }
        .markerType(IModBusEvent::class.java)
        .allowPerPhasePost()
        .build()

    override fun getEventBus() = bus

    override fun constructMod() {
        val env = NeoModLoader.environment
        val args = mapOf<Class<*>, Any?>(
            Environment::class.java to env
        )

        modInfo.owningFile.info.invokeEntrypoints(
            LoadingConstants.ENTRYPOINT_COMMON,
            NeoCommonInitializer::class.java,
            args
        ) {
            // TODO invoke onInitialize
        }

        when (env) {
            Environment.CLIENT -> modInfo.owningFile.info.invokeEntrypoints(
                LoadingConstants.ENTRYPOINT_CLIENT,
                NeoClientInitializer::class.java,
                args
            ) {
                // TODO invoke onInitializeClient
            }
            // TODO server-only entrypoint
            Environment.SERVER -> {}/*modInfo.owningFile.info.invokeEntrypoints(
                LoadingConstants.ENTRYPOINT_SERVER,
                NeoServerInitializer::class.java,
                mapOf(
                    Environment::class.java to env
                )
            ) {
                // TODO invoke onInitializeServer
            }*/
        }
    }
}