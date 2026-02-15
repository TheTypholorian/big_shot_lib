package net.typho.big_shot_lib.api.client.rendering.buffers

import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.*

interface DynamicBufferRegistry {
    fun register(buffer: DynamicBuffer)

    fun get(id: ResourceIdentifier): DynamicBuffer?

    /*
    private val buffers = HashMap<Int, DynamicBuffer>()

    fun register(buffer: DynamicBuffer) {
        val attachment = pickAvailableAttachment() ?: throw UnsupportedOperationException("Unable to pick attachment for buffer ${buffer.location()}")
        buffers[attachment] = buffer
    }

    private fun pickAvailableAttachment(): Int? {
        val max = glGetInteger(GL_MAX_COLOR_ATTACHMENTS)

        repeat(max) { i ->
            if (
                glGetFramebufferAttachmentParameteri(
                    GL_FRAMEBUFFER,
                    GL_COLOR_ATTACHMENT0 + i,
                    GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE
                ) == GL_NONE && !buffers.containsKey(i)
            ) {
                return i
            }
        }

        return null
    }
     */

    companion object {
        @JvmField
        val INSTANCE: DynamicBufferRegistry = ServiceLoader.load(DynamicBufferRegistry::class.java).findFirst().orElseThrow()

        init {
            INSTANCE.register(NormalsDynamicBuffer)
            INSTANCE.register(AlbedoDynamicBuffer)
        }
    }
}