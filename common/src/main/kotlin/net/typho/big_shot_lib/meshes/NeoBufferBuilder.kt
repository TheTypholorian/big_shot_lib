package net.typho.big_shot_lib.meshes

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.MeshData
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderType
import java.lang.AutoCloseable

open class NeoBufferBuilder(
    mode: VertexFormat.Mode,
    format: VertexFormat,
    size: Int = RenderType.TRANSIENT_BUFFER_SIZE,
    val buffer: ByteBufferBuilder = ByteBufferBuilder(size),
    val builder: BufferBuilder = BufferBuilder(buffer, mode, format)
) : BuiltinVertexConsumer(builder), AutoCloseable {
    override fun close() {
        buffer.close()
    }

    fun build(): MeshData? = builder.build()

    fun buildOrThrow(): MeshData = builder.buildOrThrow()
}