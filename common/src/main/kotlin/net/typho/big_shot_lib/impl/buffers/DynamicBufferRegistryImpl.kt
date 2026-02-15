package net.typho.big_shot_lib.impl.buffers

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBuffer
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBufferRegistry
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebuffer
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.*

class DynamicBufferRegistryImpl : DynamicBufferRegistry {
    private val buffers = HashMap<Int, DynamicBuffer>()
    private val toAdd = LinkedList<DynamicBuffer>()

    override fun register(buffer: DynamicBuffer) {
        if (Minecraft.getInstance().mainRenderTarget == null) {
            toAdd.add(buffer)
        } else {
            val attachment = GlFramebuffer.MAIN.colorAttachments.size
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
}