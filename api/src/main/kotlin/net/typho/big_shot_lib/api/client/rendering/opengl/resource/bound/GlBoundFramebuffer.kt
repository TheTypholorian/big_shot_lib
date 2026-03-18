package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit.Companion.initAndGetMask
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlFramebufferStatus
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.math.rect.NeoRect2i
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glDrawBuffers
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.glCheckFramebufferStatus
import org.lwjgl.opengl.GL43.*

interface GlBoundFramebuffer : GlBoundResource<GlFramebuffer> {
    var width: Int
    var height: Int

    var defaultWidth: Int
    var defaultHeight: Int
    var defaultLayers: Int
    var defaultSamples: Int
    var defaultFixedSampleLocations: Boolean

    fun clear(vararg bits: GlClearBit)

    fun attach(index: GlAttachmentIndex, type: GlFramebufferAttachmentType, format: GlTextureFormat)

    fun attachTexture2D(index: GlAttachmentIndex, format: GlTextureFormat): GlTexture

    fun attachTexture2D(index: GlAttachmentIndex, texture: GlTexture)

    fun readBuffer(buffer: GlAttachmentIndex.Color)

    fun drawBuffers(vararg buffers: GlAttachmentIndex.Color)

    fun checkStatus(): GlFramebufferStatus

    abstract class Basic(
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
                    NeoGlStateManager.INSTANCE.viewport.push(NeoRect2i(0, 0, width, height))
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
            type: GlFramebufferAttachmentType,
            format: GlTextureFormat
        ) {
            TODO("Not yet implemented")
        }

        override fun attachTexture2D(
            index: GlAttachmentIndex,
            format: GlTextureFormat
        ): GlTexture {
            TODO("Not yet implemented")
        }

        override fun attachTexture2D(
            index: GlAttachmentIndex,
            texture: GlTexture
        ) {
            TODO("Not yet implemented")
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