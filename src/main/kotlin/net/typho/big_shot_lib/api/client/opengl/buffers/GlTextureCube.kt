package net.typho.big_shot_lib.api.client.opengl.buffers

import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import net.typho.big_shot_lib.api.client.opengl.util.InterpolationType
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.WrappingType
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import org.lwjgl.opengl.GL13.*

interface GlTextureCube : GlTexture, GlFramebufferAttachment {
    fun setInterpolation(min: InterpolationType, mag: InterpolationType = min) {
        OpenGL.INSTANCE.textureInterpolation(type(), min, mag)
    }

    fun setWrapping(s: WrappingType, t: WrappingType = s) {
        OpenGL.INSTANCE.textureWrapping(type(), s, t)
    }

    fun resize(face: Face, width: Int, height: Int): BufferUploader

    enum class Face(
        @JvmField
        val glId: Int,
        @JvmField
        val dir: Direction
    ) : GlNamed {
        POS_X(GL_TEXTURE_CUBE_MAP_POSITIVE_X, Direction.EAST),
        NEG_X(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, Direction.WEST),
        POS_Y(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, Direction.UP),
        NEG_Y(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, Direction.DOWN),
        POS_Z(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, Direction.SOUTH),
        NEG_Z(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, Direction.NORTH);

        override fun glId() = glId
    }
}