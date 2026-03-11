package net.typho.big_shot_lib.api.client.util.quads

import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.util.NeoColor

@JvmRecord
data class BasicVertexData(
    override val pos: AbstractVec3<Float, *>,
    override val color: NeoColor?,
    override val textureUV: AbstractVec2<Float, *>?,
    override val overlayUV: AbstractVec2<Int, *>?,
    override val lightUV: AbstractVec2<Int, *>?,
    override val normal: AbstractVec3<Float, *>?
) : NeoVertexData