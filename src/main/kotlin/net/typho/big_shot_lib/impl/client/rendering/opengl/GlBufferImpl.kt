package net.typho.big_shot_lib.impl.client.rendering.opengl

//? if <1.21.5 {
/*import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlResourceType
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL15.glBufferSubData
import java.nio.ByteBuffer

class GlBufferImpl(
    override val size: Long,
    override val usage: GlBufferUsage,
    override val target: GlBufferTarget,
    override val glId: Int = GlResourceType.BUFFER.create()
) : GlBuffer {
    override val type: GlResourceType
        get() = GlResourceType.BUFFER
    override var freed: Boolean = false
        private set

    init {
        target.pushBoundValue(glId) {
            glBufferData(target.glId, size, usage.glId)
        }
    }

    override fun close() {
        if (!freed) {
            freed = true
            GlQueue.INSTANCE.runOrQueue { type.destroy(glId) }
        }
    }

    override fun uploadToSection(buffer: ByteBuffer, offset: Long) {
        target.pushBoundValue(glId) {
            glBufferSubData(target.glId, offset, buffer)
        }
    }
}
*///? }