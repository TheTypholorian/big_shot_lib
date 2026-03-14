package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER
import org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER

enum class ShaderSourceType(
    @JvmField
    val inRenderPipeline: Boolean,
    @JvmField
    val extension: String,
    override val glId: Int
) : GlNamed {
    VERTEX(true, "vsh", GL_VERTEX_SHADER),
    GEOMETRY(true, "gsh", GL_GEOMETRY_SHADER),
    FRAGMENT(true, "fsh", GL_FRAGMENT_SHADER),
    COMPUTE(false, "csh", GL_COMPUTE_SHADER);

    companion object {
        @JvmStatic
        fun fromExtension(ext: String) = entries.first { type -> type.extension == ext }
    }
}