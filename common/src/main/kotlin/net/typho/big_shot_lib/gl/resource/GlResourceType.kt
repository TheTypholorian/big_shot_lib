package net.typho.big_shot_lib.gl.resource

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL31.*
import org.lwjgl.opengl.GL40.*
import org.lwjgl.opengl.GL43.*
import org.lwjgl.opengl.GL44.GL_QUERY_BUFFER
import org.lwjgl.opengl.GL45.glBindTextureUnit
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.IntStream

open class GlResourceType(
    val glName: Int,
    val namespace: Int,
    private val bind: Consumer<Int>,
    private val unbind: Runnable
) {
    constructor(
        name: Int,
        namespace: Int,
        bind: BiConsumer<Int, Int>
    ) : this(
        name,
        namespace,
        { bind.accept(name, it) },
        { bind.accept(name, 0) }
    )

    constructor(
        name: Int,
        namespace: Int,
        bind: Consumer<Int>
    ) : this(
        name,
        namespace,
        { bind.accept(it) },
        { bind.accept(0) }
    )

    open fun label(id: Int, label: String) {
        glEnable(GL_DEBUG_OUTPUT)
        glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS)
        glObjectLabel(namespace, id, label)
    }

    open fun bind(id: Int) = bind.accept(id)

    open fun unbind() = unbind.run()

    @OptIn(ExperimentalStdlibApi::class)
    override fun toString(): String = "GlResourceType[${glName.toHexString()}]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GlResourceType

        return glName == other.glName
    }

    override fun hashCode(): Int {
        return glName
    }

    companion object {
        @JvmField
        val ARRAY_BUFFER = GlResourceType(GL_ARRAY_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val ELEMENT_ARRAY_BUFFER = GlResourceType(GL_ELEMENT_ARRAY_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val PIXEL_PACK_BUFFER = GlResourceType(GL_PIXEL_PACK_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val PIXEL_UNPACK_BUFFER = GlResourceType(GL_PIXEL_UNPACK_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val TEXTURE_BUFFER_STORAGE = GlResourceType(GL_TEXTURE_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val COPY_READ_BUFFER = GlResourceType(GL_COPY_READ_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val COPY_WRITE_BUFFER = GlResourceType(GL_COPY_WRITE_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val DRAW_INDIRECT_BUFFER = GlResourceType(GL_DRAW_INDIRECT_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val DISPATCH_INDIRECT_BUFFER = GlResourceType(GL_DISPATCH_INDIRECT_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val QUERY_BUFFER = GlResourceType(GL_QUERY_BUFFER, GL_BUFFER, ::glBindBuffer)

        @JvmField
        val TRANSFORM_FEEDBACK_BUFFER = GlIndexedBufferType(GL_TRANSFORM_FEEDBACK_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val UNIFORM_BUFFER = GlIndexedBufferType(GL_UNIFORM_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val ATOMIC_COUNTER_BUFFER = GlIndexedBufferType(GL_ATOMIC_COUNTER_BUFFER, GL_BUFFER, ::glBindBuffer)
        @JvmField
        val SHADER_STORAGE_BUFFER = GlIndexedBufferType(GL_SHADER_STORAGE_BUFFER, GL_BUFFER, ::glBindBuffer)

        @JvmField
        val VERTEX_ARRAY = GlResourceType(GL_VERTEX_ARRAY, GL_VERTEX_ARRAY, ::glBindVertexArray)

        @JvmField
        val TEXTURE_1D = GlResourceType(GL_TEXTURE_1D, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_2D = GlResourceType(GL_TEXTURE_2D, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_2D_MULTISAMPLE = GlResourceType(GL_TEXTURE_2D_MULTISAMPLE, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_3D = GlResourceType(GL_TEXTURE_3D, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_CUBE_MAP = GlResourceType(GL_TEXTURE_CUBE_MAP, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_RECTANGLE = GlResourceType(GL_TEXTURE_RECTANGLE, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_BUFFER_VIEW = GlResourceType(GL_TEXTURE_BUFFER, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_1D_ARRAY = GlResourceType(GL_TEXTURE_1D_ARRAY, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_2D_ARRAY = GlResourceType(GL_TEXTURE_2D_ARRAY, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_2D_MULTISAMPLE_ARRAY = GlResourceType(GL_TEXTURE_2D_MULTISAMPLE_ARRAY, GL_TEXTURE, ::glBindTexture)
        @JvmField
        val TEXTURE_CUBE_MAP_ARRAY = GlResourceType(GL_TEXTURE_CUBE_MAP_ARRAY, GL_TEXTURE, ::glBindTexture)

        @JvmField
        val TEXTURE_UNITS = array(
            GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS,
            { it },
            GL_TEXTURE,
            ::glBindTextureUnit
        )
        @JvmField
        val SAMPLERS = array(
            GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS,
            { GL_TEXTURE0 + it },
            GL_SAMPLER,
            { name, id ->
                GlStateManager._activeTexture(name)
                GlStateManager._bindTexture(id)
            }
        )

        @JvmField
        val FRAMEBUFFER = GlResourceType(GL_FRAMEBUFFER, GL_FRAMEBUFFER, ::glBindFramebuffer)
        @JvmField
        val READ_FRAMEBUFFER = GlResourceType(GL_READ_FRAMEBUFFER, GL_FRAMEBUFFER, ::glBindFramebuffer)
        @JvmField
        val DRAW_FRAMEBUFFER = GlResourceType(GL_DRAW_FRAMEBUFFER, GL_FRAMEBUFFER, ::glBindFramebuffer)

        @JvmField
        val RENDERBUFFER = GlResourceType(GL_RENDERBUFFER, GL_RENDERBUFFER, ::glBindRenderbuffer)

        @JvmField
        val TRANSFORM_FEEDBACK = GlResourceType(GL_TRANSFORM_FEEDBACK, GL_TRANSFORM_FEEDBACK, ::glBindTransformFeedback)

        @JvmField
        val PROGRAM = GlResourceType(GL_PROGRAM, GL_PROGRAM, ::glUseProgram)
        @JvmField
        val PROGRAM_PIPELINE = GlResourceType(GL_PROGRAM_PIPELINE, GL_PROGRAM_PIPELINE, ::glBindProgramPipeline)

        @JvmStatic
        fun array(
            number: Int,
            glName: Function<Int, Int>,
            namespace: Int,
            bind: BiConsumer<Int, Int>
        ): Array<GlResourceType> {
            return IntStream.range(0, number - 1)
                .mapToObj { GlResourceType(glName.apply(it), namespace, bind) }
                .toArray { arrayOfNulls(number - 1) }
        }
    }
}
