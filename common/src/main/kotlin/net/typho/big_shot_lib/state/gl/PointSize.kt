package net.typho.big_shot_lib.state.gl

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11.*

object PointSize : GlState<Float> {
    @JvmField
    val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "point_size")
    @JvmField
    val CODEC: Codec<Float> = Codec.FLOAT

    override fun location() = LOCATION

    override fun default() = 1f

    override fun queryValue(): Float {
        return glGetFloat(GL_POINT_SIZE)
    }

    override fun set(value: Float) {
        glPointSize(value)
    }
}
