package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAttachmentIndex
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit.Companion.initAndGetMask
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlFramebufferStatus
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlRenderbuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL11.GL_NONE
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glReadBuffer
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.opengl.GL20.glDrawBuffers
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.GL_RENDERBUFFER
import org.lwjgl.opengl.GL30.glCheckFramebufferStatus
import org.lwjgl.opengl.GL30.glFramebufferRenderbuffer
import org.lwjgl.opengl.GL30.glFramebufferTexture2D
import org.lwjgl.opengl.GL43.GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS
import org.lwjgl.opengl.GL43.GL_FRAMEBUFFER_DEFAULT_HEIGHT
import org.lwjgl.opengl.GL43.GL_FRAMEBUFFER_DEFAULT_LAYERS
import org.lwjgl.opengl.GL43.GL_FRAMEBUFFER_DEFAULT_SAMPLES
import org.lwjgl.opengl.GL43.GL_FRAMEBUFFER_DEFAULT_WIDTH
import org.lwjgl.opengl.GL43.glFramebufferParameteri
import org.lwjgl.opengl.GL43.glGetFramebufferParameteri

interface GlBoundFramebuffer : GlBoundResource<GlFramebuffer> {
    var defaultWidth: Int
    var defaultHeight: Int
    var defaultLayers: Int
    var defaultSamples: Int
    var defaultFixedSampleLocations: Boolean

    fun clear(vararg bits: GlClearBit)

    fun attach(index: GlAttachmentIndex, texture: GlTexture, level: Int)

    fun attach(index: GlAttachmentIndex, renderbuffer: GlRenderbuffer)

    fun readBuffer(buffer: GlAttachmentIndex.Color)

    fun drawBuffers(vararg buffers: GlAttachmentIndex.Color)

    fun checkStatus(): GlFramebufferStatus

    open class Basic(
        override val resource: GlFramebuffer,
        @JvmField
        val viewport: Boolean,
        override val handle: GlStateStack.Handle<Int>
    ) : GlBoundFramebuffer {
        override var defaultWidth: Int
            get() = assertBound { glGetFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_WIDTH) }
            set(value) = assertBound { glFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_WIDTH, value) }
        override var defaultHeight: Int
            get() = assertBound { glGetFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_HEIGHT) }
            set(value) = assertBound { glFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_WIDTH, value) }
        override var defaultLayers: Int
            get() = assertBound { glGetFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_LAYERS) }
            set(value) = assertBound { glFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_WIDTH, value) }
        override var defaultSamples: Int
            get() = assertBound { glGetFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_SAMPLES) }
            set(value) = assertBound { glFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_WIDTH, value) }
        override var defaultFixedSampleLocations: Boolean
            get() = assertBound { glGetFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS) == GL_TRUE }
            set(value) = assertBound { glFramebufferParameteri(GL_FRAMEBUFFER, GL_FRAMEBUFFER_DEFAULT_WIDTH, if (value) GL_TRUE else GL_FALSE) }

        init {
            if (viewport) {
                assertBound {
                    glViewport()
                }
            }
        }

        override fun clear(vararg bits: GlClearBit) {
            assertBound {
                glClear(bits.initAndGetMask())
            }
        }

        override fun attach(
            index: GlAttachmentIndex,
            texture: GlTexture,
            level: Int
        ) {
            assertBound {
                glFramebufferTexture2D(GL_FRAMEBUFFER, index.glId, GL_TEXTURE_2D, texture.glId, level)
            }
        }

        override fun attach(
            index: GlAttachmentIndex,
            renderbuffer: GlRenderbuffer
        ) {
            assertBound {
                glFramebufferRenderbuffer(GL_FRAMEBUFFER, index.glId, GL_RENDERBUFFER, renderbuffer.glId)
            }
        }

        override fun readBuffer(buffer: GlAttachmentIndex.Color) {
            assertBound {
                glReadBuffer(buffer.glId)
            }
        }

        override fun drawBuffers(vararg buffers: GlAttachmentIndex.Color) {
            assertBound {
                glDrawBuffers(buffers.map { it.glId }.ifEmpty { listOf(GL_NONE) }.toIntArray())
            }
        }

        override fun checkStatus(): GlFramebufferStatus {
            return assertBound { GlNamed.getEnum(glCheckFramebufferStatus(GL_FRAMEBUFFER)) }
        }
    }
}