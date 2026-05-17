package net.typho.big_shot_lib.impl.client.util

import net.minecraft.client.Minecraft
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.AddReloadListenerEvent
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.util.RenderLevelStage
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.NeoCamera
import net.typho.big_shot_lib.api.math.vec.NeoVec2f
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.impl.client.rendering.opengl.state.NeoGlStateManagerImpl
import net.typho.big_shot_lib.mixin.impl.FrustumAccessor

object BigShotClientEvents {
    //? if <1.21.9 {
    @JvmField
    val debugScreenInfo = arrayListOf<Pair<Boolean, (out: (line: String) -> Unit) -> Unit>>()
    //? }

    internal fun init() {
    }

    //? neoforge {
    init {
        NeoForge.EVENT_BUS.addListener { event: RenderLevelStageEvent ->
            for (entrypoint in BigShotClientEntrypoint.entrypoints) {
                entrypoint.renderLevel(
                    when (event.stage) {
                        RenderLevelStageEvent.Stage.AFTER_SKY -> RenderLevelStage.AFTER_SKY
                        RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS -> RenderLevelStage.AFTER_SOLID_BLOCKS
                        RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS -> RenderLevelStage.AFTER_CUTOUT_MIPPED_BLOCKS
                        RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS -> RenderLevelStage.AFTER_CUTOUT_BLOCKS
                        RenderLevelStageEvent.Stage.AFTER_ENTITIES -> RenderLevelStage.AFTER_ENTITIES
                        RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES -> RenderLevelStage.AFTER_BLOCK_ENTITIES
                        RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS -> RenderLevelStage.AFTER_TRANSLUCENT_BLOCKS
                        RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS -> RenderLevelStage.AFTER_TRIPWIRE_BLOCKS
                        RenderLevelStageEvent.Stage.AFTER_PARTICLES -> RenderLevelStage.AFTER_PARTICLES
                        RenderLevelStageEvent.Stage.AFTER_WEATHER -> RenderLevelStage.AFTER_WEATHER
                        RenderLevelStageEvent.Stage.AFTER_LEVEL -> RenderLevelStage.AFTER_LEVEL
                        else -> return@addListener
                    },
                    event.levelRenderer,
                    NeoCamera(
                        NeoVec3f(event.camera.position),
                        NeoVec2f(event.camera.xRot, event.camera.yRot),
                        event.camera.rotation()
                    ),
                    event.levelRenderer.level,
                    event.projectionMatrix,
                    event.modelViewMatrix,
                    (event.frustum as FrustumAccessor).`big_shot_lib$getFrustmIntersection`(),
                    NeoGlStateManagerImpl.currentTarget ?: GlFramebuffer.MAIN, // TODO
                    event.renderTick,
                    event.partialTick
                )
            }
        }
        NeoForge.EVENT_BUS.addListener { event: AddReloadListenerEvent ->
            for (entrypoint in BigShotClientEntrypoint.entrypoints) {
                entrypoint.addReloadListeners { listener ->
                    event.addListener(listener)
                }
            }
        }
        NeoForge.EVENT_BUS.addListener { event: ClientTickEvent.Pre ->
            for (entrypoint in BigShotClientEntrypoint.entrypoints) {
                entrypoint.clientTick()
            }
        }
    }

    class ScrewYouNeoforge {
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
    //? }
}