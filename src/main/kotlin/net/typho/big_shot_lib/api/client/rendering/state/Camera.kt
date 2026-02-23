package net.typho.big_shot_lib.api.client.rendering.state

import org.joml.Quaternionf
import org.joml.Vector3f

@JvmRecord
data class Camera(
    @JvmField
    val pos: Vector3f,
    @JvmField
    val xRot: Float,
    @JvmField
    val yRot: Float,
    @JvmField
    val rot: Quaternionf
)
