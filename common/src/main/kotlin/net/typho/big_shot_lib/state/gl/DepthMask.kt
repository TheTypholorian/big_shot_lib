package net.typho.big_shot_lib.state.gl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11.GL_DEPTH_WRITEMASK
import org.lwjgl.opengl.GL11.glGetBoolean

object DepthMask : GlState<Boolean> {
    @JvmField
    val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "depth_mask")
    @JvmField
    val CODEC: Codec<Boolean> = Codec.BOOL

    override fun location() = LOCATION

    override fun default() = true

    override fun queryValue(): Boolean {
        return glGetBoolean(GL_DEPTH_WRITEMASK)
    }

    override fun set(value: Boolean) {
        GlStateManager._depthMask(value)
    }
}
