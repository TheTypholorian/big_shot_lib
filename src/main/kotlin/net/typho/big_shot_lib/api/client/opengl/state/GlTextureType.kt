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
    TEXTURE_1D(GL_TEXTURE_1D, GL_TEXTURE_BINDING_1D, GlStateType(0)),
    TEXTURE_2D(GL_TEXTURE_2D, GL_TEXTURE_BINDING_2D, GlStateType(0)),
    TEXTURE_3D(GL_TEXTURE_3D, GL_TEXTURE_BINDING_3D, GlStateType(0)),
    TEXTURE_1D_ARRAY(GL_TEXTURE_1D_ARRAY, GL_TEXTURE_BINDING_1D_ARRAY, GlStateType(0), isArray = true),
    TEXTURE_2D_ARRAY(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_BINDING_2D_ARRAY, GlStateType(0), isArray = true),
    TEXTURE_RECTANGLE(GL_TEXTURE_RECTANGLE, GL_TEXTURE_BINDING_RECTANGLE, GlStateType(0)),
    TEXTURE_CUBE_MAP(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_BINDING_CUBE_MAP, GlStateType(0)),
    TEXTURE_CUBE_MAP_ARRAY(GL_TEXTURE_CUBE_MAP_ARRAY, GL_TEXTURE_BINDING_CUBE_MAP_ARRAY, GlStateType(0), isArray = true),
    TEXTURE_2D_MULTISAMPLE(GL_TEXTURE_2D_MULTISAMPLE, GL_TEXTURE_BINDING_2D_MULTISAMPLE, GlStateType(0), isMultisampled = true),
    TEXTURE_2D_MULTISAMPLE_ARRAY(GL_TEXTURE_2D_MULTISAMPLE_ARRAY, GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY, GlStateType(0), isMultisampled = true, isArray = true),
}