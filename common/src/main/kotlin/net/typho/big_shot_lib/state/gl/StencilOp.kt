package net.typho.big_shot_lib.state.gl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11.*

class StencilOp(
    var sfail: IntAction,
    var dpfail: IntAction,
    var dppass: IntAction
) : GlState<StencilOp> {
    companion object {
        @JvmField
        val DEFAULT = StencilOp(IntAction.DEFAULT, IntAction.DEFAULT, IntAction.DEFAULT)
        @JvmField
        val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "stencil_op")
        @JvmField
        val CODEC: MapCodec<StencilOp> = RecordCodecBuilder.mapCodec {
            it.group(
                IntAction.CODEC.fieldOf("stencil_fail").forGetter { color -> color.sfail },
                IntAction.CODEC.fieldOf("depth_fail").forGetter { color -> color.dpfail },
                IntAction.CODEC.fieldOf("depth_pass").forGetter { color -> color.dppass }
            ).apply(it, ::StencilOp)
        }
    }

    override fun location() = LOCATION

    override fun default() = DEFAULT

    override fun queryValue(): StencilOp {
        val sfail = glGetInteger(GL_STENCIL_FAIL)
        val dpfail = glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL)
        val dppass = glGetInteger(GL_STENCIL_PASS_DEPTH_PASS)
        return StencilOp(
            IntAction.entries.find { it.id == sfail }!!,
            IntAction.entries.find { it.id == dpfail }!!,
            IntAction.entries.find { it.id == dppass }!!
        )
    }

    override fun set(value: StencilOp) {
        GlStateManager._stencilOp(value.sfail.id, value.dpfail.id, value.dppass.id)
    }
}
