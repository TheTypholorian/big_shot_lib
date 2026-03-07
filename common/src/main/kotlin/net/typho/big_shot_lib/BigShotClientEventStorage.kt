package net.typho.big_shot_lib

import net.minecraft.client.gui.components.debug.DebugScreenDisplayer
import net.minecraft.client.gui.components.debug.DebugScreenEntry
import net.minecraft.client.renderer.CubeMap
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.DebugScreenFactory
import net.typho.big_shot_lib.api.client.util.PanoramaFactory
import net.typho.big_shot_lib.api.client.util.events.ClientEventFactory
import net.typho.big_shot_lib.api.client.util.events.ClientLevelChangedEvent
import net.typho.big_shot_lib.api.client.util.events.RenderEvent
import net.typho.big_shot_lib.api.client.util.events.WindowResizeEvent
import net.typho.big_shot_lib.api.client.util.panoramas.PanoramaSet
import net.typho.big_shot_lib.api.client.util.panoramas.PanoramaTexture
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.impl.util.PanoramaTextureStorage
import net.typho.big_shot_lib.mixin.DebugScreenEntriesAccessor
import java.util.*
import java.util.function.Consumer

object BigShotClientEventStorage : ClientEventFactory, DebugScreenFactory, PanoramaFactory {
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
    @JvmField
    val panoramaSets = LinkedList<PanoramaSet>()
    @JvmField
    val panoramaCubeMaps = HashMap<PanoramaTexture, CubeMap>()
    @JvmField
    var panorama: PanoramaSet? = null

    init {
        BigShotClientEntrypoint.registerEvents(this)
        BigShotClientEntrypoint.registerDebugScreenInfo(this)
        BigShotClientEntrypoint.registerPanoramas(this)
        panorama = PanoramaSet.select(*panoramaSets.toTypedArray())
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
        location: ResourceIdentifier,
        allowedWithReducedDebugInfo: Boolean,
        out: Consumer<Consumer<String>>
    ) {
        DebugScreenEntriesAccessor.register(location.toMojang(), object : DebugScreenEntry {
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

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun register(panorama: PanoramaSet) {
        panoramaSets.add(panorama)

        for (texture in panorama.textures) {
            panoramaCubeMaps.computeIfAbsent(texture) { key ->
                val map = CubeMap(BigShotApi.id("dummy").toMojang())
                (map as PanoramaTextureStorage).`big_shot_lib$panorama_texture` = key
                return@computeIfAbsent map
            }
        }
    }
}