package net.typho.big_shot_lib.api.client.event

import net.minecraft.client.Camera
import net.minecraft.client.DeltaTracker
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.LevelRenderer
import org.joml.FrustumIntersection
import org.joml.Matrix4f

fun interface RenderLevelEvent {
    fun renderLevel(
        levelRenderer: LevelRenderer,
        camera: Camera,
        level: ClientLevel,
        projMat: Matrix4f,
        modelViewMat: Matrix4f,
        frustum: FrustumIntersection?,
        //target: GlFramebuffer,
        partialTick: DeltaTracker
    )
}