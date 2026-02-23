package net.typho.big_shot_lib.api.client.opengl.shaders

import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER
import org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER
import org.lwjgl.util.shaderc.Shaderc.*
import org.lwjgl.util.spvc.Spv.*

enum class ShaderSourceType(
    @JvmField
    val inRenderPipeline: Boolean,
    @JvmField
    val extension: String,
    @JvmField
    val glId: Int,
    @JvmField
    val shadercId: Int,
    @JvmField
    val spvcId: Int?
) {
    VERTEX(true, "vsh", GL_VERTEX_SHADER, shaderc_vertex_shader, SpvExecutionModelVertex),
    GEOMETRY(true, "gsh", GL_GEOMETRY_SHADER, shaderc_geometry_shader, SpvExecutionModelGeometry),
    FRAGMENT(true, "fsh", GL_FRAGMENT_SHADER, shaderc_fragment_shader, SpvExecutionModelFragment),
    COMPUTE(false, "csh", GL_COMPUTE_SHADER, shaderc_compute_shader, null);

    companion object {
        @JvmStatic
        fun fromExtension(ext: String) = entries.first { type -> type.extension == ext }

        @JvmStatic
        fun fromGlId(id: Int) = entries.first { type -> type.glId == id }
    }
}