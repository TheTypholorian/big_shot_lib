package net.typho.big_shot_lib.state.gl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11.*

enum class PolygonMode(val id: Int) : GlState<PolygonMode> {
    POINT(GL_POINT),
    LINE(GL_LINE),
    FILL(GL_FILL);

    companion object {
        @JvmField
        val DEFAULT = FILL
        @JvmField
        val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "polygon_mode")
        @JvmField
        val CODEC: Codec<PolygonMode> = Codec.STRING.xmap(
            { key -> enumValueOf<PolygonMode>(key) },
            { equation -> equation.name }
        )
    }

    override fun location() = LOCATION

    override fun default() = DEFAULT

    override fun queryValue(): PolygonMode? {
        val mode = glGetInteger(GL_POLYGON_MODE)
        return PolygonMode.entries.find { it.id == mode }
    }

    override fun set(value: PolygonMode) {
        GlStateManager._polygonMode(GL_FRONT_AND_BACK, value.id)
    }
}
