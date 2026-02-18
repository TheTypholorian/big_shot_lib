package net.typho.big_shot_lib.impl.buffers

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBuffer
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBufferRegistry
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixinManager
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebuffer
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.lwjgl.opengl.GL11.GL_BLEND
import org.lwjgl.opengl.GL30.glDisablei
import org.lwjgl.opengl.GL30.glEnablei
import java.util.*

class DynamicBufferRegistryImpl : DynamicBufferRegistry {
    val buffers = HashMap<Int, DynamicBuffer>() // TODO
    private val toAdd = LinkedList<DynamicBuffer>()

    override fun register(buffer: DynamicBuffer) {
        ShaderMixinManager.register(buffer)

        if (Minecraft.getInstance().mainRenderTarget == null) {
            toAdd.add(buffer)
        } else {
            // TODO
            val attachment = buffers.size //GlFramebuffer.MAIN.colorAttachments.size
            buffers[attachment] = buffer
            GlFramebuffer.MAIN.colorAttachments += buffer
        }
    }

    override fun get(id: ResourceIdentifier): DynamicBuffer? {
        return buffers.values.firstOrNull { it.location() == id }
    }

    fun init() {
        for (buffer in toAdd) {
            val attachment = GlFramebuffer.MAIN.colorAttachments.size
            buffers[attachment] = buffer
            GlFramebuffer.MAIN.colorAttachments += buffer
        }

        toAdd.clear()
    }

    fun enableBlend() {
        for (entry in buffers) {
            if (entry.value.blend()) {
                glEnablei(GL_BLEND, entry.key)
            } else {
                glDisablei(GL_BLEND, entry.key)
            }
        }
    }

    fun disableBlend() {
        for (entry in buffers) {
            glDisablei(GL_BLEND, entry.key)
        }
    }
}