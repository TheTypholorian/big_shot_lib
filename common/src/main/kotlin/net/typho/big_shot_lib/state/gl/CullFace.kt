package net.typho.big_shot_lib.state.gl

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11.*

enum class CullFace(val id: Int) : GlState<CullFace> {
    FRONT(GL_FRONT),
    BACK(GL_BACK),
    FRONT_AND_BACK(GL_FRONT_AND_BACK);

    companion object {
        @JvmField
        val DEFAULT = BACK
        @JvmField
        val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "cull_face")
        @JvmField
        val CODEC: Codec<CullFace> = Codec.STRING.xmap(
            { key -> enumValueOf<CullFace>(key) },
            { face -> face.name }
        )
    }

    override fun location() = LOCATION

    override fun default() = DEFAULT

    override fun queryValue(): CullFace? {
        val face = glGetInteger(GL_CULL_FACE)
        return CullFace.entries.find { it.id == face }
    }

    override fun set(value: CullFace) {
        glCullFace(value.id)
    }
}
