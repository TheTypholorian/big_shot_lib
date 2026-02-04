package net.typho.big_shot_lib.api.render_queue.shards

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.GlManager
import net.typho.big_shot_lib.api.render_queue.RenderSettingShard
import net.typho.big_shot_lib.api.util.IColor
import net.typho.big_shot_lib.api.util.NeoCodecs

open class BlendShard(
    @JvmField
    val enabled: Boolean,
    @JvmField
    val color: IColor,
    @JvmField
    val equation: BlendEquation,
    @JvmField
    val function: BlendFunction
) : RenderSettingShard.Basic(
    BlendShard,
    if (enabled) listOf(
        GlFlag.BLEND.bindable,
        Bindable.ofState(GlManager::blendColor, color, IColor.BLACK),
        equation,
        function
    ) else listOf()
) {
    companion object : RenderSettingShard.Type<BlendShard> {
        override fun getDefault() = BlendShard(
            false,
            IColor.BLACK,
            BlendEquation.ADD,
            BlendFunction.Basic(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA)
        )

        override fun codec(): MapCodec<BlendShard> = RecordCodecBuilder.mapCodec {
            it.group(
                IColor.CODEC_ANY.fieldOf("color").forGetter { shard -> shard.color },
                NeoCodecs.enumCodec<BlendEquation>().fieldOf("equation").forGetter { shard -> shard.equation },
                BlendFunction.CODEC.fieldOf("function").forGetter { shard -> shard.function },
            ).apply(it) { color, eq, func -> BlendShard(true, color, eq, func) }
        }

        override fun location(): ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "blend")
    }
}