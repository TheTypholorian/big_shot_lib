package net.typho.big_shot_lib.api.client.rendering.opengl.util

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlStencilAction
import net.typho.big_shot_lib.api.util.resource.NeoCodecs

@JvmRecord
data class StencilOp(
    @JvmField
    val stencilFail: GlStencilAction,
    @JvmField
    val depthFail: GlStencilAction,
    @JvmField
    val depthPass: GlStencilAction
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<StencilOp> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<GlStencilAction>().fieldOf("stencil_fail").forGetter { color -> color.stencilFail },
                NeoCodecs.enumCodec<GlStencilAction>().fieldOf("depth_fail").forGetter { color -> color.depthFail },
                NeoCodecs.enumCodec<GlStencilAction>().fieldOf("depth_pass").forGetter { color -> color.depthPass }
            ).apply(it, ::StencilOp)
        }
    }
}