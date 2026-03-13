package net.typho.big_shot_lib.api.client.opengl.state

import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL43.*

enum class GlTextureType(
    @JvmField
    val state: GlStateType<Int>,
    @JvmField
    val bindingId: Int,
    @JvmField
    val isMultisampled: Boolean = false,
    @JvmField
    val isArray: Boolean = false
) {
    TEXTURE_1D(GlStateType(GL_TEXTURE_1D, 0, "Texture1D"), GL_TEXTURE_BINDING_1D),
    TEXTURE_2D(GlStateType(GL_TEXTURE_2D, 0, "Texture2D"), GL_TEXTURE_BINDING_2D),
    TEXTURE_3D(GlStateType(GL_TEXTURE_3D, 0, "Texture3D"), GL_TEXTURE_BINDING_3D),
    TEXTURE_1D_ARRAY(GlStateType(GL_TEXTURE_1D_ARRAY, 0, "Texture1DArray"), GL_TEXTURE_BINDING_1D_ARRAY, isArray = true),
    TEXTURE_2D_ARRAY(GlStateType(GL_TEXTURE_2D_ARRAY, 0, "Texture2DArray"), GL_TEXTURE_BINDING_2D_ARRAY, isArray = true),
    TEXTURE_RECTANGLE(GlStateType(GL_TEXTURE_RECTANGLE, 0, "TextureRectangle"), GL_TEXTURE_BINDING_RECTANGLE),
    TEXTURE_CUBE_MAP(GlStateType(GL_TEXTURE_CUBE_MAP, 0, "TextureCubeMap"), GL_TEXTURE_BINDING_CUBE_MAP),
    TEXTURE_CUBE_MAP_ARRAY(GlStateType(GL_TEXTURE_CUBE_MAP_ARRAY, 0, "TextureCubeMapArray"), GL_TEXTURE_BINDING_CUBE_MAP_ARRAY, isArray = true),
    TEXTURE_2D_MULTISAMPLE(GlStateType(GL_TEXTURE_2D_MULTISAMPLE, 0, "Texture2DMultisample"), GL_TEXTURE_BINDING_2D_MULTISAMPLE, isMultisampled = true),
    TEXTURE_2D_MULTISAMPLE_ARRAY(GlStateType(GL_TEXTURE_2D_MULTISAMPLE_ARRAY, 0, "Texture2DMultisampleArray"), GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY, isMultisampled = true, isArray = true),
}