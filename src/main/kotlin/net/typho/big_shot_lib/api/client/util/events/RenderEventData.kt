package net.typho.big_shot_lib.api.client.util.events

import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.level.Level
import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebuffer
import net.typho.big_shot_lib.api.client.opengl.state.Camera
import org.joml.FrustumIntersection
import org.joml.Matrix4f

data class RenderEventData(
    @JvmField
    val buffers: MultiBufferSource,
    @JvmField
    val camera: Camera,
    @JvmField
    val level: Level,
    @JvmField
    val projMat: Matrix4f,
    @JvmField
    val inverseProjMat: Matrix4f,
    @JvmField
    val modelViewMat: Matrix4f,
    @JvmField
    val inverseModelViewMat: Matrix4f,
    @JvmField
    val frustum: FrustumIntersection,
    @JvmField
    val target: GlFramebuffer
)
