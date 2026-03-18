package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit.Companion.initAndGetMask
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlFramebufferStatus
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebufferAttachment
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.KeyedDelegate
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glDrawBuffers
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.glCheckFramebufferStatus
import org.lwjgl.opengl.GL43.*

interface GlBoundFramebuffer : GlBoundResource<GlFramebuffer> {
    var defaultWidth: Int
    var defaultHeight: Int
    var defaultLayers: Int
    var defaultSamples: Int
    var defaultFixedSampleLocations: Boolean

    val colorAttachments: KeyedDelegate<Int, GlFramebufferAttachment?>
    var depthAttachment: GlFramebufferAttachment?

    fun clear(vararg bits: GlClearBit)

    fun readBuffer(buffer: Int)

    fun drawBuffers(vararg buffers: Int)

    fun checkStatus(): GlFramebufferStatus

    abstract class Basic(
        override val resource: GlFramebuffer,
        @JvmField
        val viewport: AbstractRect2<Int, *, *>?,
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
            viewport?.let {
                NeoGlStateManager.INSTANCE.viewport.push(it)
            }
        }

        override fun clear(vararg bits: GlClearBit) {
            assertBound {
                glClear(bits.initAndGetMask())
            }
        }

        override fun readBuffer(buffer: Int) {
            assertBound {
                glReadBuffer(buffer)
            }
        }

        override fun drawBuffers(vararg buffers: Int) {
            assertBound {
                glDrawBuffers(buffers)
            }
        }

        override fun checkStatus(): GlFramebufferStatus {
            return assertBound { GlNamed.getEnum(glCheckFramebufferStatus(GL_FRAMEBUFFER)) }
        }

        override fun unbind() {
            if (viewport != null) {
                NeoGlStateManager.INSTANCE.viewport.pop()
            }

            super.unbind()
        }
    }
}