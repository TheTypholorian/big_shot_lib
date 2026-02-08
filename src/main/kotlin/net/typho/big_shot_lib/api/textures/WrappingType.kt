package net.typho.big_shot_lib.api.textures

import net.typho.big_shot_lib.api.GlNamed
import org.lwjgl.opengl.GL11.GL_REPEAT
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER
import org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT
import org.lwjgl.opengl.GL44.GL_MIRROR_CLAMP_TO_EDGE

enum class WrappingType(
    @JvmField
    val glId: Int
): GlNamed {
    REPEAT(GL_REPEAT),
    MIRRORED_REPEAT(GL_MIRRORED_REPEAT),
    CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
    CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER),
    MIRROR_CLAMP_TO_EDGE(GL_MIRROR_CLAMP_TO_EDGE);

    override fun glId() = glId
}