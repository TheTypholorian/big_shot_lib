package net.typho.big_shot_lib.state.gl

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11.GL_DEPTH_FUNC
import org.lwjgl.opengl.GL11.glGetInteger

object DepthTest : GlState<ComparisonMode> {
    @JvmField
    val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "depth_test")
    @JvmField
    val CODEC = ComparisonMode.CODEC

    override fun location() = LOCATION

    override fun default() = ComparisonMode.LEQUAL

    override fun queryValue(): ComparisonMode? {
        val depth = glGetInteger(GL_DEPTH_FUNC)
        return ComparisonMode.entries.find { it.id == depth }
    }

    override fun set(value: ComparisonMode) {
        GlStateManager._depthFunc(value.id)
    }
}
