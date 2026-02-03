package net.typho.big_shot_lib.state.gl

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11.*

object LineWidth : GlState<Float> {
    @JvmField
    val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "line_width")
    @JvmField
    val CODEC: Codec<Float> = Codec.FLOAT

    override fun location() = LOCATION

    override fun default() = 1f

    override fun queryValue(): Float {
        return glGetFloat(GL_LINE_WIDTH)
    }

    override fun set(value: Float) {
        glLineWidth(value)
    }
}
