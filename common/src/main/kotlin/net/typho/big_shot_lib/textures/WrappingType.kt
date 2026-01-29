package net.typho.big_shot_lib.textures

import com.mojang.serialization.Codec
import org.lwjgl.opengl.GL11.GL_REPEAT
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER
import org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT
import org.lwjgl.opengl.GL44.GL_MIRROR_CLAMP_TO_EDGE

enum class WrappingType(val id: Int) {
    REPEAT(GL_REPEAT),
    MIRRORED_REPEAT(GL_MIRRORED_REPEAT),
    CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
    CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER),
    MIRROR_CLAMP_TO_EDGE(GL_MIRROR_CLAMP_TO_EDGE);

    companion object {
        @JvmStatic
        fun from(id: Int): WrappingType? {
            for (type in WrappingType.entries) {
                if (type.id == id) {
                    return type
                }
            }

            return null
        }

        @JvmField
        val CODEC: Codec<WrappingType> = Codec.STRING.xmap(
            { string -> enumValueOf(string) },
            { format -> format.name }
        )
    }
}