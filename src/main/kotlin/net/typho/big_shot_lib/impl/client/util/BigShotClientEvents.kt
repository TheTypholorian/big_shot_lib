package net.typho.big_shot_lib.impl.client.util

//? fabric {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.server.packs.PackType
import net.typho.big_shot_lib.impl.mojang
//? if <1.21.9 {
/*import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
*///? } else {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
//? }
//? } neoforge {
/*import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.level.ChunkEvent
//? if <1.21.9 {
import net.neoforged.neoforge.client.event.RenderLevelStageEvent

//? if >1.21.5 {
/*import org.joml.Matrix4f
import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.impl.util.getExtensionValue
*///? }
//? }
//? if <1.21.4 {
import net.neoforged.neoforge.event.AddReloadListenerEvent
//? } else {
/*import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
*///? }
*///? }

import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.DebugScreenFactory
import net.typho.big_shot_lib.api.client.util.NeoCamera
import net.typho.big_shot_lib.api.client.util.ResourceListenerFactory
import net.typho.big_shot_lib.api.client.util.event.*
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.math.vec.NeoVec2f
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.impl.client.rendering.opengl.state.NeoGlStateManagerImpl
import net.typho.big_shot_lib.mixin.impl.FrustumAccessor
import org.joml.Matrix4f

//? if >=1.21.9 {
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer
import net.minecraft.client.gui.components.debug.DebugScreenEntry
import net.typho.big_shot_lib.mixin.impl.DebugScreenEntriesAccessor
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk
//? }

object BigShotClientEvents : ResourceListenerFactory, ClientEventFactory, DebugScreenFactory {
    override val clientTickStart: MutableList<Runnable> = arrayListOf()
    override val clientTickEnd: MutableList<Runnable> = arrayListOf()
    override val levelRenderEnd: MutableList<RenderEvent> = arrayListOf()
    override val levelChanged: MutableList<ClientLevelChangedEvent> = arrayListOf()
    override val chunkChanged: MutableList<ChunkChangedEvent> = arrayListOf()
    //? if <1.21.9 {
    /*@JvmField
    val debugScreenInfo = arrayListOf<Pair<Boolean, (out: (line: String) -> Unit) -> Unit>>()
    *///? }

    //? neoforge {
    /*val reloadListeners = arrayListOf<NeoResourceManagerReloadListener>()
    var listenersLoaded = false
    *///? }

    internal fun init() {
        BigShotClientEntrypoint.registerReloadListeners(this)
        BigShotClientEntrypoint.registerEvents(this)
        BigShotClientEntrypoint.registerDebugScreenInfo(this)

        //? fabric {
        //? if <1.21.9 {
        /*WorldRenderEvents.LAST.register { context ->
            val data = RenderEventData(
                NeoCamera(
                    NeoVec3f(context.camera().position),
                    NeoVec2f(context.camera().xRot, context.camera().yRot),
                    context.camera().rotation()
                ),
                context.world(),
                context.projectionMatrix(),
                //? if >=1.21 {
                context.positionMatrix(),
                //? } else {
                /*context.matrixStack()!!.last().pose(),
                *///? }
                (context.frustum() as FrustumAccessor).`big_shot_lib$getFrustmIntersection`(),
                NeoGlStateManagerImpl.currentTarget ?: GlFramebuffer.MAIN
            )
            levelRenderEnd.forEach { it.invoke(data) }
        }
        *///? }
        ClientTickEvents.START_CLIENT_TICK.register { clientTickStart.forEach { it.run() } }
        ClientTickEvents.END_CLIENT_TICK.register { clientTickEnd.forEach { it.run() } }
        ClientChunkEvents.CHUNK_LOAD.register { level, chunk -> chunkChanged.forEach { it.invoke(level, null, chunk) } }
        ClientChunkEvents.CHUNK_UNLOAD.register { level, chunk -> chunkChanged.forEach { it.invoke(level, chunk, null) } }
        //? }
    }

    override fun register(listener: NeoResourceManagerReloadListener) {
        //? fabric {
        BigShotApi.LOGGER.info("Registering reload listener ${listener.location}")
        //? if <1.21.9 {
        /*ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
            override fun getFabricId() = listener.location.mojang

            override fun onResourceManagerReload(manager: ResourceManager) {
                listener.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(manager))
            }
        })
        *///? } else {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(listener.location.mojang, object : ResourceManagerReloadListener {
            override fun onResourceManagerReload(manager: ResourceManager) {
                listener.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(manager))
            }
        })
        //? }
        //? } neoforge {
        /*if (listenersLoaded) {
            throw IllegalStateException("Attempted to registered resource listener ${listener.location} after add resource listeners event has been fired")
        }

        BigShotApi.LOGGER.info("Queueing reload listener ${listener.location}")

        reloadListeners.add(listener)
        *///? }
    }

    override fun register(
        location: NeoIdentifier,
        allowedWithReducedDebugInfo: Boolean,
        text: (out: (line: String) -> Unit) -> Unit
    ) {
        //? if <1.21.9 {
        /*debugScreenInfo.add(allowedWithReducedDebugInfo to text)
        *///? } else {
        DebugScreenEntriesAccessor.`big_shot_lib$register`(location.mojang, object : DebugScreenEntry {
            override fun display(
                displayer: DebugScreenDisplayer,
                level: Level?,
                clientChunk: LevelChunk?,
                serverChunk: LevelChunk?
            ) {
                text(displayer::addLine)
            }

            override fun isAllowed(reducedDebugInfo: Boolean): Boolean {
                return allowedWithReducedDebugInfo || !reducedDebugInfo
            }
        })
        //? }
    }

    //? neoforge {
    /*init {
        NeoForge.EVENT_BUS.addListener { event: RenderLevelStageEvent ->
            if (event.stage == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                if (levelRenderEnd.isNotEmpty()) {
                    val data = RenderEventData(
                        NeoCamera(
                            NeoVec3f(event.camera.position),
                            NeoVec2f(event.camera.xRot, event.camera.yRot),
                            event.camera.rotation()
                        ),
                        event.levelRenderer.level,
                        event.projectionMatrix,
                        event.modelViewMatrix,
                        (event.frustum as FrustumAccessor).`big_shot_lib$getFrustmIntersection`(),
                        NeoGlStateManagerImpl.currentTarget ?: GlFramebuffer.MAIN
                    )
                    levelRenderEnd.forEach { it.invoke(data) }
                }
            }
        }
        NeoForge.EVENT_BUS.addListener { event: ChunkEvent.Load ->
            chunkChanged.forEach { it.invoke(event.level, null, event.chunk) }
        }
        NeoForge.EVENT_BUS.addListener { event: ChunkEvent.Unload ->
            chunkChanged.forEach { it.invoke(event.level, event.chunk, null) }
        }
        NeoForge.EVENT_BUS.addListener { event: AddReloadListenerEvent ->
            listenersLoaded = true
            BigShotApi.LOGGER.info("Registering reload listeners")
            for (listener in reloadListeners) {
                BigShotApi.LOGGER.info("Actually registering reload listener ${listener.location}")
                event.addListener(object : ResourceManagerReloadListener {
                    override fun onResourceManagerReload(manager: ResourceManager) {
                        listener.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(Minecraft.getInstance().resourceManager))
                    }
                })
            }
        }
    }

    class ScrewYouNeoforge {
        @SubscribeEvent
        fun preClientTick(event: ClientTickEvent.Pre) {
            clientTickStart.forEach { it.run() }
        }

        @SubscribeEvent
        fun postClientTick(event: ClientTickEvent.Post) {
            clientTickEnd.forEach { it.run() }
        }

        //? if >1.21.5 && <1.21.9 {
        /*@SubscribeEvent
        fun postRender(event: RenderLevelStageEvent.AfterLevel) {
            if (levelRenderEnd.isNotEmpty()) {
                val data = RenderEventData(
                    NeoCamera(
                        NeoVec3f(event.camera.position),
                        NeoVec2f(event.camera.xRot, event.camera.yRot),
                        event.camera.rotation()
                    ),
                    event.levelRenderer.level,
                    Matrix4f(
                        RenderSystem.getProjectionMatrixBuffer()!!
                            .buffer
                            .getExtensionValue<GlBuffer>()
                            .bind(GlBufferTarget.ARRAY_BUFFER)
                            .use { it.getBufferData(0L, 16L * Float.SIZE_BYTES) }
                            .asByteBuffer()
                            .asFloatBuffer()
                    ),
                    event.modelViewMatrix,
                    (event.frustum as FrustumAccessor).`big_shot_lib$getFrustmIntersection`(),
                    NeoGlStateManagerImpl.currentTarget ?: GlFramebuffer.MAIN
                )
                levelRenderEnd.forEach { it.invoke(data) }
            }
        }
        *///? }
    }
    *///? }
}