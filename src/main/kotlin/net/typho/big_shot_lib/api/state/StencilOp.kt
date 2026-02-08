package net.typho.big_shot_lib.api.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.util.NeoCodecs

@JvmRecord
data class StencilOp(
    @JvmField
    val stencilFail: IntAction,
    @JvmField
    val depthFail: IntAction,
    @JvmField
    val depthPass: IntAction
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<StencilOp> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<IntAction>().fieldOf("stencil_fail").forGetter { color -> color.stencilFail },
                NeoCodecs.enumCodec<IntAction>().fieldOf("depth_fail").forGetter { color -> color.depthFail },
                NeoCodecs.enumCodec<IntAction>().fieldOf("depth_pass").forGetter { color -> color.depthPass }
            ).apply(it, ::StencilOp)
        }
    }
}