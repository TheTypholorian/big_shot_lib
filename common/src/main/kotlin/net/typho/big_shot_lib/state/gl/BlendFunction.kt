package net.typho.big_shot_lib.state.gl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL14.*

class BlendFunction(
    var src: BlendFactor,
    var dst: BlendFactor
) : GlState<BlendFunction> {
    companion object {
        @JvmField
        val DEFAULT = BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA)
        @JvmField
        val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "blend_function")
        @JvmField
        val CODEC: MapCodec<BlendFunction> = RecordCodecBuilder.mapCodec {
            it.group(
                BlendFactor.CODEC.fieldOf("src").forGetter { func -> func.src },
                BlendFactor.CODEC.fieldOf("dst").forGetter { func -> func.dst }
            ).apply(it, ::BlendFunction)
        }
    }

    override fun location() = LOCATION

    override fun default() = DEFAULT

    override fun queryValue(): BlendFunction {
        val src = glGetInteger(GL_BLEND_SRC_RGB)
        val dst = glGetInteger(GL_BLEND_DST_RGB)
        return BlendFunction(
            BlendFactor.entries.find { it.id == src }!!,
            BlendFactor.entries.find { it.id == dst }!!
        )
    }

    override fun set(value: BlendFunction) {
        GlStateManager._blendFunc(value.src.id, value.dst.id)
    }
}
