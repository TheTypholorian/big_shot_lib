package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL31.GL_TEXTURE_BINDING_RECTANGLE
import org.lwjgl.opengl.GL31.GL_TEXTURE_RECTANGLE
import org.lwjgl.opengl.GL44.GL_TEXTURE_2D_MULTISAMPLE
import org.lwjgl.opengl.GL44.GL_TEXTURE_BINDING_2D_MULTISAMPLE

enum class GlTextureTarget(
    override val glId: Int,
    @JvmField
    val bindingId: Int,
    @JvmField
    val dimensions: Int?,
    @JvmField
    val multisample: Boolean
) : GlConstant {
    TEXTURE_1D(GL_TEXTURE_1D, GL_TEXTURE_BINDING_1D, 1, false),
    TEXTURE_2D(GL_TEXTURE_2D, GL_TEXTURE_BINDING_2D, 2, false),
    TEXTURE_3D(GL_TEXTURE_3D, GL_TEXTURE_BINDING_3D, 3, false),
    TEXTURE_RECTANGLE(GL_TEXTURE_RECTANGLE, GL_TEXTURE_BINDING_RECTANGLE, null, false),
    TEXTURE_CUBE_MAP(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_BINDING_CUBE_MAP, null, false),
    TEXTURE_2D_MULTISAMPLE(GL_TEXTURE_2D_MULTISAMPLE, GL_TEXTURE_BINDING_2D_MULTISAMPLE, 2, true)
}