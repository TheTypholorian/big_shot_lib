package net.typho.big_shot_lib.impl.client.util

//? fabric {
/*import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.server.packs.PackType
//? if <1.21.9 {
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
//? } else {
/*import net.fabricmc.fabric.api.resource.v1.ResourceLoader
*///? }
*///? } neoforge {
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.common.NeoForge
//? if <1.21.4 {
import net.neoforged.neoforge.event.AddReloadListenerEvent
//? } else {
/*import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
*///? }
//? }

import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
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
import net.typho.big_shot_lib.impl.mixin.FrustumAccessor
import net.typho.big_shot_lib.impl.mixin.LevelRendererAccessor

//? if >=1.21.9 {
/*import net.minecraft.client.gui.components.debug.DebugScreenDisplayer
import net.minecraft.client.gui.components.debug.DebugScreenEntry
import net.typho.big_shot_lib.impl.mixin.DebugScreenEntriesAccessor
*///? }

object BigShotClientEvents : ResourceListenerFactory, ClientEventFactory, DebugScreenFactory {
    override val clientTickStart: MutableList<Runnable> = arrayListOf()
    override val clientTickEnd: MutableList<Runnable> = arrayListOf()
    override val levelRenderEnd: MutableList<RenderEvent> = arrayListOf()
    override val levelChanged: MutableList<ClientLevelChangedEvent> = arrayListOf()
    override val chunkChanged: MutableList<ChunkChangedEvent> = arrayListOf()
    //? if <1.21.9 {
    @JvmField
    val debugScreenInfo = arrayListOf<Pair<Boolean, (out: (line: String) -> Unit) -> Unit>>()
    //? }

    init {
        BigShotClientEntrypoint.registerReloadListeners(this)
        BigShotClientEntrypoint.registerEvents(this)
        BigShotClientEntrypoint.registerDebugScreenInfo(this)

        levelRenderEnd.add {
            fun check(stack: GlStateStack<*>) {
                if (stack.size > 0) {
                    throw IllegalStateException("Didn't pop gl state stack")
                }
            }

            NeoGlStateManager.INSTANCE.buffers.values.forEach { check(it) }
            NeoGlStateManager.INSTANCE.textures.values.forEach { check(it) }

            check(NeoGlStateManager.INSTANCE.program)
            check(NeoGlStateManager.INSTANCE.vertexArray)
            check(NeoGlStateManager.INSTANCE.renderbuffer)
            check(NeoGlStateManager.INSTANCE.framebuffer)
            check(NeoGlStateManager.INSTANCE.readFramebuffer)

            check(NeoGlStateManager.INSTANCE.blendEnabled)
            check(NeoGlStateManager.INSTANCE.colorLogicOpEnabled)
            check(NeoGlStateManager.INSTANCE.cullFaceEnabled)
            //check(NeoGlStateManager.INSTANCE.debugOutputEnabled)
            //check(NeoGlStateManager.INSTANCE.debugOutputSynchronousEnabled)
            check(NeoGlStateManager.INSTANCE.depthClampEnabled)
            check(NeoGlStateManager.INSTANCE.depthEnabled)
            check(NeoGlStateManager.INSTANCE.ditherEnabled)
            check(NeoGlStateManager.INSTANCE.framebufferSRGBEnabled)
            check(NeoGlStateManager.INSTANCE.lineSmoothEnabled)
            check(NeoGlStateManager.INSTANCE.multisampleEnabled)
            check(NeoGlStateManager.INSTANCE.polygonOffsetEnabled)
            check(NeoGlStateManager.INSTANCE.polygonSmoothEnabled)
            check(NeoGlStateManager.INSTANCE.primitiveRestartEnabled)
            check(NeoGlStateManager.INSTANCE.primitiveRestartFixedIndexEnabled)
            check(NeoGlStateManager.INSTANCE.rasterizerDiscardEnabled)
            check(NeoGlStateManager.INSTANCE.sampleAlphaToCoverageEnabled)
            check(NeoGlStateManager.INSTANCE.sampleAlphaToOneEnabled)
            check(NeoGlStateManager.INSTANCE.sampleCoverageEnabled)
            check(NeoGlStateManager.INSTANCE.sampleShadingEnabled)
            check(NeoGlStateManager.INSTANCE.sampleMaskEnabled)
            check(NeoGlStateManager.INSTANCE.scissorEnabled)
            check(NeoGlStateManager.INSTANCE.stencilEnabled)
            check(NeoGlStateManager.INSTANCE.textureCubeMapSeamlessEnabled)
            check(NeoGlStateManager.INSTANCE.programPointSizeEnabled)
        }

        //? fabric {
        /*//? if <1.21.9 {
        WorldRenderEvents.LAST.register { context ->
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
        //? }
        ClientTickEvents.START_CLIENT_TICK.register { clientTickStart.forEach { it.run() } }
        ClientTickEvents.END_CLIENT_TICK.register { clientTickEnd.forEach { it.run() } }
        ClientChunkEvents.CHUNK_LOAD.register { level, chunk -> chunkChanged.forEach { it.invoke(level, null, chunk) } }
        ClientChunkEvents.CHUNK_UNLOAD.register { level, chunk -> chunkChanged.forEach { it.invoke(level, chunk, null) } }
        *///? } neoforge {
        NeoForge.EVENT_BUS.addListener { event: ClientTickEvent.Pre ->
            clientTickStart.forEach { it.run() }
        }
        NeoForge.EVENT_BUS.addListener { event: ClientTickEvent.Post ->
            clientTickEnd.forEach { it.run() }
        }
        //? if <=1.21.5 {
        NeoForge.EVENT_BUS.addListener { event: RenderLevelStageEvent ->
            if (event.stage == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                if (levelRenderEnd.isNotEmpty()) {
                    val data = RenderEventData(
                        NeoCamera(
                            NeoVec3f(event.camera.position),
                            NeoVec2f(event.camera.xRot, event.camera.yRot),
                            event.camera.rotation()
                        ),
                        (event.levelRenderer as LevelRendererAccessor).`big_shot_lib$getLevel`(),
                        event.projectionMatrix,
                        event.modelViewMatrix,
                        (event.frustum as FrustumAccessor).`big_shot_lib$getFrustmIntersection`(),
                        NeoGlStateManagerImpl.currentTarget ?: GlFramebuffer.MAIN
                    )
                    levelRenderEnd.forEach { it.invoke(data) }
                }
            }
        }
        //? } else if <1.21.9 {
        /*NeoForge.EVENT_BUS.addListener { event: RenderLevelStageEvent.AfterLevel ->
            if (levelRenderEnd.isNotEmpty()) {
                val data = RenderEventData(
                    NeoCamera(
                        NeoVec3f(event.camera.position),
                        NeoVec2f(event.camera.xRot, event.camera.yRot),
                        event.camera.rotation()
                    ),
                    (event.levelRenderer as LevelRendererAccessor).`big_shot_lib$getLevel`(),
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
        //? }
    }

    override fun register(listener: NeoResourceManagerReloadListener) {
        //? fabric {
        /*//? if <1.21.9 {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
            override fun getFabricId() = listener.location.mojang

            override fun onResourceManagerReload(manager: ResourceManager) {
                listener.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(manager))
            }
        })
        //? } else {
        /*ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(listener.location.mojang, object : ResourceManagerReloadListener {
            override fun onResourceManagerReload(manager: ResourceManager) {
                listener.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(manager))
            }
        })
        *///? }
        *///? } neoforge {
        //? if <1.21.4 {
        NeoForge.EVENT_BUS.addListener { event: AddReloadListenerEvent ->
            event.addListener(object : ResourceManagerReloadListener {
                override fun onResourceManagerReload(manager: ResourceManager) {
                    listener.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(manager))
                }
            })
        }
        //? } else {
        /*NeoForge.EVENT_BUS.addListener { event: AddClientReloadListenersEvent ->
            event.addListener(listener.location.mojang, object : ResourceManagerReloadListener {
                override fun onResourceManagerReload(manager: ResourceManager) {
                    listener.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(manager))
                }
            })
        }
        *///? }
        //? }
    }

    override fun register(
        location: NeoIdentifier,
        allowedWithReducedDebugInfo: Boolean,
        text: (out: (line: String) -> Unit) -> Unit
    ) {
        //? if <1.21.9 {
        debugScreenInfo.add(allowedWithReducedDebugInfo to text)
        //? } else {
        /*DebugScreenEntriesAccessor.`big_shot_lib$register`(location.mojang, object : DebugScreenEntry {
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
        *///? }
    }

    @JvmStatic
    internal fun init() = Unit
}