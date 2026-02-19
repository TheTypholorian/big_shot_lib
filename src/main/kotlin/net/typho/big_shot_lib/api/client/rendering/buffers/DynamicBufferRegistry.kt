package net.typho.big_shot_lib.api.client.rendering.buffers

import org.lwjgl.opengl.GL11.GL_BLEND
import org.lwjgl.opengl.GL30.glDisablei
import org.lwjgl.opengl.GL30.glEnablei

object DynamicBufferRegistry {
    @JvmField
    val buffers = HashMap<Int, DynamicBuffer>()

    init {
        register(NormalsDynamicBuffer)
        register(AlbedoDynamicBuffer)
    }

    @JvmStatic
    fun register(buffer: DynamicBuffer) {
        val location = buffers.size + 1
        buffers[location] = buffer
        buffer.setShaderLocation(location)
    }

    @JvmStatic
    fun enableBlend() {
        for (entry in buffers) {
            if (entry.value.blend()) {
                glEnablei(GL_BLEND, entry.key)
            } else {
                glDisablei(GL_BLEND, entry.key)
            }
        }
    }

    @JvmStatic
    fun disableBlend() {
        for (entry in buffers) {
            glDisablei(GL_BLEND, entry.key)
        }
    }

    fun init() = Unit
}