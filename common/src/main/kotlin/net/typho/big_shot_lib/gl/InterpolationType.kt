package net.typho.big_shot_lib.gl

import com.mojang.serialization.Codec
import org.lwjgl.opengl.GL11.GL_LINEAR
import org.lwjgl.opengl.GL11.GL_NEAREST

enum class InterpolationType(val id: Int) {
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR);

    companion object {
        @JvmField
        val CODEC: Codec<InterpolationType> = Codec.STRING.xmap(
            { string -> enumValueOf(string) },
            { format -> format.name }
        )
    }
}