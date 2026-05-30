package net.typho.big_shot_lib.api.client.rendering.opengl.resource

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.plugin.Namespace
import java.nio.ByteBuffer

@Namespace(BigShotApi.MOD_ID)
interface GlBuffer : GlResource {
    val size: Long
    val usage: GlBufferUsage
    val target: GlBufferTarget

    fun bind(target: GlBufferTarget) {
        NeoGlStateManager.INSTANCE.buffers[target] = glId
    }

    fun unbind(target: GlBufferTarget) {
        NeoGlStateManager.INSTANCE.buffers[target] = 0
    }

    fun slice() = slice(0, size)

    fun slice(offset: Long = 0, size: Long = this.size - offset): Slice {
        return Slice(this, offset, size)
    }

    fun upload(buffer: ByteBuffer) = uploadToSection(buffer, 0)

    fun uploadToSection(buffer: ByteBuffer, offset: Long)

    data class Slice(
        @JvmField
        val buffer: GlBuffer,
        @JvmField
        val offset: Long,
        @JvmField
        val length: Long
    )

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke(size: Long, usage: GlBufferUsage, target: GlBufferTarget) = InternalUtil.INSTANCE.createBuffer(size, usage, target)
    }
}