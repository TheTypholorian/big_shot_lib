package net.typho.big_shot_lib.state.gl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11.*

class StencilFunc(
    var func: ComparisonMode,
    var ref: Int,
    var mask: Int
) : GlState<StencilFunc> {
    companion object {
        @JvmField
        val DEFAULT = StencilFunc(ComparisonMode.ALWAYS, 0xFF, 0xFF)
        @JvmField
        val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "stencil_func")
        @JvmField
        val CODEC: MapCodec<StencilFunc> = RecordCodecBuilder.mapCodec {
            it.group(
                ComparisonMode.CODEC.fieldOf("func").forGetter { color -> color.func },
                Codec.INT.fieldOf("ref").forGetter { color -> color.ref },
                Codec.INT.fieldOf("mask").forGetter { color -> color.mask }
            ).apply(it, ::StencilFunc)
        }
    }

    override fun location() = LOCATION

    override fun default() = DEFAULT

    override fun queryValue(): StencilFunc {
        val func = glGetInteger(GL_STENCIL_FUNC)
        return StencilFunc(
            ComparisonMode.entries.find { it.id == func }!!,
            glGetInteger(GL_STENCIL_REF),
            glGetInteger(GL_STENCIL_VALUE_MASK)
        )
    }

    override fun set(value: StencilFunc) {
        GlStateManager._stencilFunc(value.func.id, value.ref, value.mask)
    }
}
