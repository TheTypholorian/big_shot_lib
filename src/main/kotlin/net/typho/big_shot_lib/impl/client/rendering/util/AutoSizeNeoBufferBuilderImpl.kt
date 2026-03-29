package net.typho.big_shot_lib.impl.client.rendering.util

import com.mojang.blaze3d.vertex.PoseStack
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlIndexDataType
import net.typho.big_shot_lib.api.client.rendering.util.NeoBufferBuilder
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.util.BYTE_MASK
import net.typho.big_shot_lib.api.util.SHORT_MASK
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import net.typho.big_shot_lib.api.util.buffer.realloc
import org.joml.Vector3f

class AutoSizeNeoBufferBuilderImpl(
    override val format: NeoVertexFormat,
    override val mode: GlBeginMode
) : NeoBufferBuilder() {
    private var filledVertices: Int = 0
    private var filledElements: Int = 0
    private var currentIndex: Long = 0

    private var vertexBuffer: NeoBuffer.Native? = null
    private var indexBuffer: NeoBuffer.Native? = null

    override fun free() {
        vertexBuffer?.free()
        indexBuffer?.free()
    }

    override fun build(): Built {
        end()

        return object : Built {
            override val vertexBuffer: NeoBuffer = this@AutoSizeNeoBufferBuilderImpl.vertexBuffer!!
            override val indexBuffer: NeoBuffer? = this@AutoSizeNeoBufferBuilderImpl.indexBuffer
            override val format: NeoVertexFormat = this@AutoSizeNeoBufferBuilderImpl.format
            override val vertexCount: Int = filledVertices
            override val indexCount: Int? = this@AutoSizeNeoBufferBuilderImpl.mode.indexData?.let { vertexCount / it.multiplier * it.offsets.size }
            override val mode: GlBeginMode = this@AutoSizeNeoBufferBuilderImpl.mode
            override val indexType: GlIndexDataType? = indexCount?.let {
                when (it) {
                    it and BYTE_MASK -> GlIndexDataType.BYTE
                    it and SHORT_MASK -> GlIndexDataType.SHORT
                    else -> GlIndexDataType.INT
                }
            }
        }
    }

    override fun put(element: NeoVertexFormat.Element, data: NeoBuffer.(index: Long) -> Unit) {
        val mask = 1 shl format.elements.indexOf(element)

        if (filledElements and mask != 0) {
            throw IllegalStateException("Already filled element $element")
        }

        filledElements = filledElements or mask
        val buffer = vertexBuffer.realloc((filledVertices + 1).toLong() * format.vertexSizeBytes)
        vertexBuffer = buffer
        data(buffer, currentIndex + format.getElementOffset(element))
    }

    override fun end() {
        if (filledElements != (1 shl format.elements.size - 1)) {
            throw IllegalStateException("Missing vertex elements for $format")
        }

        filledVertices++
        currentIndex += format.vertexSizeBytes
        filledElements = 0
    }

    override fun normal(
        pose: PoseStack.Pose,
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        //? if >=1.20.5 {
        val vec = pose.transformNormal(x, y, z, Vector3f())
        return normal(vec.x, vec.y, vec.z)
        //? } else {
        /*val vec = pose.normal().transform(Vector3f(x, y, z))
        return normal(vec.x, vec.y, vec.z)
        *///? }
    }
}