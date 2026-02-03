package net.typho.big_shot_lib.state.gl

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.ARBImaging.GL_BLEND_COLOR
import org.lwjgl.opengl.GL11.glGetFloatv
import org.lwjgl.opengl.GL14.glBlendColor

class BlendColor(
    var red: Float,
    var green: Float,
    var blue: Float,
    var alpha: Float,
) : GlState<BlendColor> {
    companion object {
        @JvmField
        val DEFAULT = BlendColor(0f, 0f, 0f, 0f)
        @JvmField
        val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "blend_color")
        @JvmField
        val CODEC: MapCodec<BlendColor> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.FLOAT.fieldOf("red").forGetter { color -> color.red },
                Codec.FLOAT.fieldOf("green").forGetter { color -> color.green },
                Codec.FLOAT.fieldOf("blue").forGetter { color -> color.blue },
                Codec.FLOAT.fieldOf("alpha").forGetter { color -> color.alpha }
            ).apply(it, ::BlendColor)
        }
    }

    override fun location() = LOCATION

    override fun default() = DEFAULT

    override fun queryValue(): BlendColor {
        val color = FloatArray(4)
        glGetFloatv(GL_BLEND_COLOR, color)
        return BlendColor(color[0], color[1], color[2], color[3])
    }

    override fun set(value: BlendColor) {
        glBlendColor(value.red, value.green, value.blue, value.alpha)
    }
}
