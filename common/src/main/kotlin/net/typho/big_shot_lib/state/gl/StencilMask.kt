package net.typho.big_shot_lib.state.gl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11.GL_STENCIL_WRITEMASK
import org.lwjgl.opengl.GL11.glGetInteger

object StencilMask : GlState<Int> {
    @JvmField
    val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "stencil_mask")
    @JvmField
    val CODEC: Codec<Int> = Codec.INT

    override fun location() = LOCATION

    override fun default() = 0xFF

    override fun queryValue(): Int {
        return glGetInteger(GL_STENCIL_WRITEMASK)
    }

    override fun set(value: Int) {
        GlStateManager._stencilMask(value)
    }
}
