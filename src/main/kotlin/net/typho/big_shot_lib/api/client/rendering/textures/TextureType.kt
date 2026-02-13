package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.util.GlNamed
import org.lwjgl.opengl.GL11.GL_TEXTURE_1D
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL12.GL_TEXTURE_3D
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER
import org.lwjgl.opengl.GL31.GL_TEXTURE_RECTANGLE
import org.lwjgl.opengl.GL40.*

enum class TextureType(
    @JvmField
    val glId: Int
) : GlNamed {
    ONE_D(GL_TEXTURE_1D),
    TWO_D(GL_TEXTURE_2D),
    THREE_D(GL_TEXTURE_3D),
    ONE_D_ARRAY(GL_TEXTURE_1D_ARRAY),
    TWO_D_ARRAY(GL_TEXTURE_2D_ARRAY),
    RECT(GL_TEXTURE_RECTANGLE),
    CUBE_MAP(GL_TEXTURE_CUBE_MAP),
    CUBE_MAP_ARRAY(GL_TEXTURE_CUBE_MAP_ARRAY),
    TWO_D_MULTISAMPLE(GL_TEXTURE_2D_MULTISAMPLE),
    TWO_D_MULTISAMPLE_ARRAY(GL_TEXTURE_2D_MULTISAMPLE_ARRAY),
    BUFFER(GL_TEXTURE_BUFFER);

    override fun glId() = glId
}