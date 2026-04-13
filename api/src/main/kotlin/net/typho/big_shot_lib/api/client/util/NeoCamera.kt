package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.math.vec.IVec2
import net.typho.big_shot_lib.api.math.vec.IVec3
import org.joml.Quaternionf

data class NeoCamera(
    @JvmField
    val pos: IVec3<Float>,
    @JvmField
    val xyRot: IVec2<Float>,
    @JvmField
    val qRot: Quaternionf
)