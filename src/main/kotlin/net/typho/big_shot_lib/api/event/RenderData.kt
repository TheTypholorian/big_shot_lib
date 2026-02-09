package net.typho.big_shot_lib.api.event

import net.minecraft.client.Camera
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.level.Level
import org.joml.FrustumIntersection
import org.joml.Matrix4f

data class RenderData(
    @JvmField
    val buffers: MultiBufferSource,
    @JvmField
    val camera: Camera,
    @JvmField
    val level: Level,
    @JvmField
    val projMat: Matrix4f,
    @JvmField
    val modelViewMat: Matrix4f,
    @JvmField
    val frustum: FrustumIntersection
)
