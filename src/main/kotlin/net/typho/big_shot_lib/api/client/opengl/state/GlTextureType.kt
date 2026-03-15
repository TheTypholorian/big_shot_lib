package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL43.*

enum class GlTextureType(
    override val glId: Int,
    @JvmField
    val bindingId: Int,
    @JvmField
    val state: GlStateType<Int>,
    @JvmField
    val isMultisampled: Boolean = false,
    @JvmField
    val isArray: Boolean = false
) : GlNamed {
    TEXTURE_1D(GL_TEXTURE_1D, GL_TEXTURE_BINDING_1D, GlStateType(GlStateTracker::texture1DBinding)),
    TEXTURE_2D(GL_TEXTURE_2D, GL_TEXTURE_BINDING_2D, GlStateType(GlStateTracker::texture2DBinding)),
    TEXTURE_3D(GL_TEXTURE_3D, GL_TEXTURE_BINDING_3D, GlStateType(GlStateTracker::texture3DBinding)),
    TEXTURE_1D_ARRAY(GL_TEXTURE_1D_ARRAY, GL_TEXTURE_BINDING_1D_ARRAY, GlStateType(GlStateTracker::texture1DArrayBinding), isArray = true),
    TEXTURE_2D_ARRAY(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_BINDING_2D_ARRAY, GlStateType(GlStateTracker::texture2DArrayBinding), isArray = true),
    TEXTURE_RECTANGLE(GL_TEXTURE_RECTANGLE, GL_TEXTURE_BINDING_RECTANGLE, GlStateType(GlStateTracker::textureRectangleBinding)),
    TEXTURE_CUBE_MAP(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_BINDING_CUBE_MAP, GlStateType(GlStateTracker::textureCubeMapBinding)),
    TEXTURE_CUBE_MAP_ARRAY(GL_TEXTURE_CUBE_MAP_ARRAY, GL_TEXTURE_BINDING_CUBE_MAP_ARRAY, GlStateType(GlStateTracker::textureCubeMapArrayBinding), isArray = true),
    TEXTURE_2D_MULTISAMPLE(GL_TEXTURE_2D_MULTISAMPLE, GL_TEXTURE_BINDING_2D_MULTISAMPLE, GlStateType(GlStateTracker::texture2DMultisampleBinding), isMultisampled = true),
    TEXTURE_2D_MULTISAMPLE_ARRAY(GL_TEXTURE_2D_MULTISAMPLE_ARRAY, GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY, GlStateType(
        GlStateTracker::texture2DMultisampleArrayBinding
    ), isMultisampled = true, isArray = true),
}