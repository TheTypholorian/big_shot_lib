package net.typho.big_shot_lib

import net.minecraft.client.gui.components.debug.DebugScreenDisplayer
import net.minecraft.client.gui.components.debug.DebugScreenEntry
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.api.client.registration.BigShotClientRegistrationEntrypoint
import net.typho.big_shot_lib.api.client.registration.DebugScreenFactory
import net.typho.big_shot_lib.api.client.registration.events.ClientEventFactory
import net.typho.big_shot_lib.api.client.registration.events.ClientLevelChangedEvent
import net.typho.big_shot_lib.api.client.registration.events.RenderEvent
import net.typho.big_shot_lib.api.client.registration.events.WindowResizeEvent
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.mixin.DebugScreenEntriesAccessor
import java.util.*
import java.util.function.Consumer

object BigShotClientEventStorage : ClientEventFactory, DebugScreenFactory {
    @JvmField
    val onFrameStart = LinkedList<Runnable>()
    @JvmField
    val onLevelRenderEnd = LinkedList<RenderEvent>()
    @JvmField
    val onFrameEnd = LinkedList<Runnable>()
    @JvmField
    val onWindowResized = LinkedList<WindowResizeEvent>()
    @JvmField
    val onLevelChanged = LinkedList<ClientLevelChangedEvent>()

    init {
        BigShotClientRegistrationEntrypoint.registerEvents(this)
        BigShotClientRegistrationEntrypoint.registerDebugScreenInfo(this)
    }

    override fun onFrameStart(event: Runnable) {
        onFrameStart.add(event)
    }

    override fun onLevelRenderEnd(event: RenderEvent) {
        onLevelRenderEnd.add(event)
    }

    override fun onFrameEnd(event: Runnable) {
        onFrameEnd.add(event)
    }

    override fun onWindowResized(event: WindowResizeEvent) {
        onWindowResized.add(event)
    }

    override fun onLevelChanged(event: ClientLevelChangedEvent) {
        onLevelChanged.add(event)
    }

    override fun register(
        id: ResourceIdentifier,
        allowedWithReducedDebugInfo: Boolean,
        out: Consumer<Consumer<String>>
    ) {
        DebugScreenEntriesAccessor.register(id.toMojang(), object : DebugScreenEntry {
            override fun display(
                displayer: DebugScreenDisplayer,
                level: Level?,
                clientChunk: LevelChunk?,
                serverChunk: LevelChunk?
            ) {
                out.accept(displayer::addLine)
            }

            override fun isAllowed(reducedDebugInfo: Boolean): Boolean {
                return allowedWithReducedDebugInfo || !reducedDebugInfo
            }
        })
    }
}