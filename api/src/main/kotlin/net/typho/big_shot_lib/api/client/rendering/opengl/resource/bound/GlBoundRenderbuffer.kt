package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.*
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlRenderbuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import org.lwjgl.opengl.GL42.*

interface GlBoundRenderbuffer : GlBoundResource<GlRenderbuffer> {
    val width: Int
    val height: Int
    val internalFormat: GlTextureFormat
    val redSize: Int
    val greenSize: Int
    val blueSize: Int
    val alphaSize: Int
    val depthSize: Int
    val stencilSize: Int
    val samples: Int

    fun renderbufferStorage(
        format: GlTextureFormat,
        width: Int,
        height: Int
    )

    fun renderbufferStorageMultisample(
        format: GlTextureFormat,
        width: Int,
        height: Int,
        samples: Int
    )

    open class Basic(
        override val resource: GlRenderbuffer,
        override val handle: GlStateStack.Handle<Int>
    ) : GlBoundRenderbuffer {
        override val width: Int
            get() = assertBound { glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_WIDTH) }
        override val height: Int
            get() = assertBound { glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_HEIGHT) }
        override val internalFormat: GlTextureFormat
            get() = assertBound { GlTextureFormat.fromInternalId(glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_INTERNAL_FORMAT))!! }
        override val redSize: Int
            get() = assertBound { glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_RED_SIZE) }
        override val greenSize: Int
            get() = assertBound { glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_GREEN_SIZE) }
        override val blueSize: Int
            get() = assertBound { glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_BLUE_SIZE) }
        override val alphaSize: Int
            get() = assertBound { glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_ALPHA_SIZE) }
        override val depthSize: Int
            get() = assertBound { glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_DEPTH_SIZE) }
        override val stencilSize: Int
            get() = assertBound { glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_STENCIL_SIZE) }
        override val samples: Int
            get() = assertBound { glGetRenderbufferParameteri(GL_RENDERBUFFER, GL_RENDERBUFFER_SAMPLES) }

        override fun toString(): String {
            return "Bound($resource)"
        }

        override fun renderbufferStorage(
            format: GlTextureFormat,
            width: Int,
            height: Int
        ) {
            assertBound {
                glRenderbufferStorage(GL_RENDERBUFFER, format.internalId, width, height)
            }
        }

        override fun renderbufferStorageMultisample(
            format: GlTextureFormat,
            width: Int,
            height: Int,
            samples: Int
        ) {
            assertBound {
                glRenderbufferStorageMultisample(GL_RENDERBUFFER, format.internalId, width, height, samples)
            }
        }
    }
}