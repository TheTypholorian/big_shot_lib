package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.opengl.GL15.glUnmapBuffer
import org.lwjgl.system.NativeResource

class GlMappedBuffer(
    pointer: Long,
    size: Long,
    @JvmField
    val target: GlBufferTarget
) : NeoBuffer.Native(pointer, size, false) {
    override fun free() {
        glUnmapBuffer(target.glId)
    }
}
