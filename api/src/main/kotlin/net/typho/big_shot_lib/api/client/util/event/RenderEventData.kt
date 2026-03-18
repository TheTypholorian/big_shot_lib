package net.typho.big_shot_lib.api.client.util.event

import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.level.Level
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoCamera
import org.joml.FrustumIntersection
import org.joml.Matrix4f

data class RenderEventData(
    @JvmField
    val buffers: MultiBufferSource,
    @JvmField
    val camera: NeoCamera,
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
