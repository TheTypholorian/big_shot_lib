package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.opengl.GL15.glUnmapBuffer

class GlMappedBuffer(
    pointer: Long,
    size: Long,
    @JvmField
    val buffer: GlBoundBuffer
) : NeoBuffer.Native(pointer, size, false) {
    private class Cleanup(
        val buffer: GlBoundBuffer
    ) : Runnable {
        override fun run() {
            buffer.assertBound {
                glUnmapBuffer(buffer.target.glId)
            }
        }
    }

    override fun createCleanup(): Runnable {
        return Cleanup(buffer)
    }
}
