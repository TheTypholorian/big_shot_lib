package net.typho.big_shot_lib.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.state.gl.BlendColor
import net.typho.big_shot_lib.state.gl.BlendEquation
import net.typho.big_shot_lib.state.gl.BlendFactor
import net.typho.big_shot_lib.state.gl.BlendFunction
import java.util.*

class NeoBlendShard(
    val color: Optional<BlendColor>,
    val equation: Optional<BlendEquation>,
    val factor: Optional<BlendFactor>,
    val function: Optional<BlendFunction>
) : IRenderSetting {
    companion object {
        @JvmField
        val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("big_shot_lib", "blend")
        @JvmField
        val CODEC: MapCodec<NeoBlendShard> = RecordCodecBuilder.mapCodec {
            it.group(
                BlendColor.CODEC.codec().optionalFieldOf("color").forGetter { blend -> blend.color },
                BlendColor.CODEC.codec().optionalFieldOf("color").forGetter { blend -> blend.color },
            ).apply(it, ::NeoBlendShard)
        }
    }

    override fun location() = LOCATION

    override fun push() {
        TODO("Not yet implemented")
    }

    override fun pop() {
        TODO("Not yet implemented")
    }
}