package net.typho.big_shot_lib.api.client.opengl.util

import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL32.GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS

enum class FramebufferStatus(
    override val glId: Int,
    /**
     * @see <a href="https://docs.gl/gl4/glCheckFramebufferStatus">Descriptions taken from OpenGL docs page</a>
     */
    @JvmField
    val description: String
) : GlNamed {
    COMPLETE(
        GL_FRAMEBUFFER_COMPLETE,
        "The specified framebuffer is complete"
    ),
    UNDEFINED(
        GL_FRAMEBUFFER_UNDEFINED,
        "The specified framebuffer is the default read or draw framebuffer, but the default framebuffer does not exist"
    ),
    INCOMPLETE_ATTACHMENT(
        GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT,
        "Any of the framebuffer attachment points are framebuffer incomplete"
    ),
    INCOMPLETE_MISSING_ATTACHMENT(
        GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT ,
        "The framebuffer does not have at least one image attached to it"
    ),
    DRAW_BUFFER(
        GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER,
        "The value of GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE is GL_NONE for any color attachment point(s) named by GL_DRAW_BUFFERi"
    ),
    READ_BUFFER(
        GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER,
        "GL_READ_BUFFER is not GL_NONE and the value of GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE is GL_NONE for the color attachment point named by GL_READ_BUFFER"
    ),
    UNSUPPORTED(
        GL_FRAMEBUFFER_UNSUPPORTED,
        "The value of GL_RENDERBUFFER_SAMPLES is not the same for all attached renderbuffers; if the value of GL_TEXTURE_SAMPLES is the not same for all attached textures; or, if the attached images are a mix of renderbuffers and textures, the value of GL_RENDERBUFFER_SAMPLES does not match the value of GL_TEXTURE_SAMPLES"
    ),
    INCOMPLETE_MULTISAMPLE(
        GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE,
        "The value of GL_TEXTURE_FIXED_SAMPLE_LOCATIONS is not the same for all attached textures; or, if the attached images are a mix of renderbuffers and textures, the value of GL_TEXTURE_FIXED_SAMPLE_LOCATIONS is not GL_TRUE for all attached textures"
    ),
    INCOMPLETE_LAYER_TARGETS(
        GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS,
        "Any framebuffer attachment is layered, and any populated attachment is not layered, or if all populated color attachments are not from textures of the same target"
    ),
    UNKNOWN_ERROR(
        0,
        "An error occurred (yes I know that's super helpful)"
    );
}