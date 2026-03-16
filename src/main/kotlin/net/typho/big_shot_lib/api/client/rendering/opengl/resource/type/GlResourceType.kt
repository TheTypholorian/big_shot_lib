package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlConstant
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER
import org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER
import org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER
import org.lwjgl.opengl.GL43.*

enum class GlResourceType(
    override val glId: Int,
    @JvmField
    val create: () -> Int,
    @JvmField
    val destroy: (glId: Int) -> Unit
): GlConstant {
    BUFFER(GL_BUFFER, ::glGenBuffers, ::glDeleteBuffers),
    VERTEX_SHADER(GL_SHADER, { glCreateShader(GL_VERTEX_SHADER) }, ::glDeleteShader),
    GEOMETRY_SHADER(GL_SHADER, { glCreateShader(GL_GEOMETRY_SHADER) }, ::glDeleteShader),
    FRAGMENT_SHADER(GL_SHADER, { glCreateShader(GL_FRAGMENT_SHADER) }, ::glDeleteShader),
    TESS_CONTROL_SHADER(GL_SHADER, { glCreateShader(GL_TESS_CONTROL_SHADER) }, ::glDeleteShader),
    TESS_EVALUATION_SHADER(GL_SHADER, { glCreateShader(GL_TESS_EVALUATION_SHADER) }, ::glDeleteShader),
    COMPUTE_SHADER(GL_SHADER, { glCreateShader(GL_COMPUTE_SHADER) }, ::glDeleteShader),
    PROGRAM(GL_PROGRAM, ::glCreateProgram, ::glDeleteProgram),
    QUERY(GL_QUERY, ::glGenQueries, ::glDeleteQueries),
    PROGRAM_PIPELINE(GL_PROGRAM_PIPELINE, ::glGenProgramPipelines, ::glDeleteProgramPipelines),
    SAMPLER(GL_SAMPLER, ::glGenSamplers, ::glDeleteSamplers),
    VERTEX_ARRAY(GL_VERTEX_ARRAY, ::glGenVertexArrays, ::glDeleteVertexArrays),
    TEXTURE(GL_TEXTURE, ::glGenTextures, ::glDeleteTextures),
    RENDERBUFFER(GL_RENDERBUFFER, ::glGenRenderbuffers, ::glDeleteRenderbuffers),
    FRAMEBUFFER(GL_FRAMEBUFFER, ::glGenFramebuffers, ::glDeleteFramebuffers),
    TRANSFORM_FEEDBACK(GL_TRANSFORM_FEEDBACK, ::glGenTransformFeedbacks, ::glDeleteTransformFeedbacks);

    fun label(glId: Int, label: String) {
        glObjectLabel(this.glId, glId, label)
    }
}