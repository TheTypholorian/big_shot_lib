package net.typho.big_shot_lib.impl.client.util

//? fabric {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
//? } neoforge {
/*import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RenderFrameEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.common.NeoForge
import net.typho.big_shot_lib.impl.mixin.LevelRendererAccessor
*///? }

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoCamera
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.event.*
import net.typho.big_shot_lib.api.math.vec.NeoVec2f
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.impl.client.rendering.opengl.state.NeoGlStateManagerImpl
import net.typho.big_shot_lib.impl.mixin.FrustumAccessor

object BigShotClientEvents : ClientEventFactory {
    override val clientTickStart: MutableList<Runnable> = arrayListOf()
    override val clientTickEnd: MutableList<Runnable> = arrayListOf()
    override val levelRenderEnd: MutableList<RenderEvent> = arrayListOf()
    override val levelChanged: MutableList<ClientLevelChangedEvent> = arrayListOf()

    init {
        BigShotClientEntrypoint.registerEvents(this)

        //? fabric {
        WorldRenderEvents.LAST.register { context ->
            val data = RenderEventData(
                NeoCamera(
                    NeoVec3f(context.camera().position),
                    NeoVec2f(context.camera().xRot, context.camera().yRot),
                    context.camera().rotation()
                ),
                context.world(),
                context.projectionMatrix(),
                context.positionMatrix(),
                (context.frustum() as FrustumAccessor).`big_shot_lib$getFrustmIntersection`(),
                NeoGlStateManagerImpl.currentTarget ?: GlFramebuffer.MAIN
            )
            levelRenderEnd.forEach { it.invoke(data) }
        }
        ClientTickEvents.START_CLIENT_TICK.register { clientTickStart.forEach { it.run() } }
        ClientTickEvents.END_CLIENT_TICK.register { clientTickEnd.forEach { it.run() } }
        //? } neoforge {
        /*NeoForge.EVENT_BUS.addListener { event: ClientTickEvent.Pre ->
            clientTickStart.forEach { it.run() }
        }
        NeoForge.EVENT_BUS.addListener { event: ClientTickEvent.Post ->
            clientTickEnd.forEach { it.run() }
        }
        NeoForge.EVENT_BUS.addListener { event: RenderLevelStageEvent ->
            if (event.stage == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
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
        *///? }
    }

    @JvmStatic
    internal fun init() = Unit
}