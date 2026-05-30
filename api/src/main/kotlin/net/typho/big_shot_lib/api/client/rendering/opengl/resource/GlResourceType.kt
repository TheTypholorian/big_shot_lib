package net.typho.big_shot_lib.api.client.rendering.opengl.resource

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlConstant
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER
import org.lwjgl.opengl.GL43.*
import kotlin.reflect.KMutableProperty0

enum class GlResourceType(
    override val glId: Int,
    @JvmField
    val create: () -> Int,
    @JvmField
    val destroy: (glId: Int) -> Unit,
    @JvmField
    val property: KMutableProperty0<Int>?
): GlConstant {
    BUFFER(GL_BUFFER, ::glGenBuffers, ::glDeleteBuffers, null),
    VERTEX_SHADER(GL_SHADER, { glCreateShader(GL_VERTEX_SHADER) }, ::glDeleteShader, null),
    GEOMETRY_SHADER(GL_SHADER, { glCreateShader(GL_GEOMETRY_SHADER) }, ::glDeleteShader, null),
    FRAGMENT_SHADER(GL_SHADER, { glCreateShader(GL_FRAGMENT_SHADER) }, ::glDeleteShader, null),
    PROGRAM(GL_PROGRAM, ::glCreateProgram, ::glDeleteProgram, null),
    PROGRAM_PIPELINE(GL_PROGRAM_PIPELINE, ::glGenProgramPipelines, ::glDeleteProgramPipelines, null),
    SAMPLER(GL_SAMPLER, ::glGenSamplers, ::glDeleteSamplers, null),
    VERTEX_ARRAY(GL_VERTEX_ARRAY, ::glGenVertexArrays, ::glDeleteVertexArrays, NeoGlStateManager.INSTANCE::vertexArray),
    TEXTURE(GL_TEXTURE, ::glGenTextures, ::glDeleteTextures, NeoGlStateManager.INSTANCE::texture),
    RENDERBUFFER(GL_RENDERBUFFER, ::glGenRenderbuffers, ::glDeleteRenderbuffers, NeoGlStateManager.INSTANCE::renderbuffer),
    FRAMEBUFFER(GL_FRAMEBUFFER, ::glGenFramebuffers, ::glDeleteFramebuffers, null),
    TRANSFORM_FEEDBACK(GL_TRANSFORM_FEEDBACK, ::glGenTransformFeedbacks, ::glDeleteTransformFeedbacks, null);

    fun getBoundValue() = (property ?: throw IllegalStateException("GlResourceType $this is not bindable")).get()

    fun setBoundValue(glId: Int) {
        (property ?: throw IllegalStateException("GlResourceType $this is not bindable")).set(glId)
    }

    fun <V> pushBoundValue(glId: Int, task: () -> V): V {
        val property = property ?: throw IllegalStateException("GlResourceType $this is not bindable")
        val old = property.get()
        property.set(glId)
        val value = task()
        property.set(old)
        return value
    }

    fun label(glId: Int, label: String) {
        glObjectLabel(this.glId, glId, label)
    }
}