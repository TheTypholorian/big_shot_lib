package net.typho.big_shot_lib.impl.util

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.DynamicBufferFactory
import net.typho.big_shot_lib.api.client.util.dynamic_buffers.DynamicBuffer
import org.lwjgl.opengl.GL11.GL_BLEND
import org.lwjgl.opengl.GL30.glDisablei
import org.lwjgl.opengl.GL30.glEnablei
import java.util.*

object DynamicBufferRegistry : DynamicBufferFactory {
    @JvmField
    val buffers = LinkedList<DynamicBuffer<*>>()

    init {
        BigShotClientEntrypoint.registerDynamicBuffers(this)
    }

    override fun register(buffer: DynamicBuffer<*>): Int {
        val attachment = buffers.size + 1
        buffers.add(buffer)
        buffer.shaderLocation = attachment

        val fbo = Minecraft.getInstance().mainRenderTarget
        fbo.resize(fbo.width, fbo.height)

        return attachment
    }

    @JvmStatic
    fun enableBlend() {
        buffers.forEachIndexed { index, buffer ->
            if (buffer.blend) {
                glEnablei(GL_BLEND, index + 1)
            } else {
                glDisablei(GL_BLEND, index + 1)
            }
        }
    }

    @JvmStatic
    fun disableBlend() {
        buffers.forEachIndexed { index, buffer ->
            glDisablei(GL_BLEND, index + 1)
        }
    }
}