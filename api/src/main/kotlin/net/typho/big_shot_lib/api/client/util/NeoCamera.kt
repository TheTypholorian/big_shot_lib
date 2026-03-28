package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import org.joml.Quaternionf

data class NeoCamera(
    @JvmField
    val pos: AbstractVec3<Float>,
    @JvmField
    val xyRot: AbstractVec2<Float>,
    @JvmField
    val qRot: Quaternionf
)