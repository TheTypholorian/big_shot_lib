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
import org.joml.Vector3f

class KnownSizeNeoBufferBuilderImpl(
    override val format: NeoVertexFormat,
    override val mode: GlBeginMode,
    @JvmField
    val numVertices: Int,
    vertexBuffer: (size: Long) -> NeoBuffer.Native,
    indexBuffer: (size: Long?) ->  NeoBuffer.Native?
) : NeoBufferBuilder() {
    private var filledVertices: Int = 0
    private var filledElements: Int = 0
    private var currentIndex: Long = 0

    private val vertexBuffer = vertexBuffer(numVertices.toLong() * format.vertexSizeBytes)
    private val numIndices: Int? = mode.indexData?.let {
        if (numVertices % it.stride != 0) {
            throw IllegalArgumentException("Vertex count $numVertices is not a multiple of ${it.stride} required for $mode")
        }

        numVertices / it.stride * it.offsets.size
    }
    private val indexType: GlIndexDataType? = numIndices?.let {
        when (it) {
            it and BYTE_MASK -> GlIndexDataType.BYTE
            it and SHORT_MASK -> GlIndexDataType.SHORT
            else -> GlIndexDataType.INT
        }
    }
    private val indexBuffer = indexBuffer(numIndices?.let { it.toLong() * indexType!!.sizeBytes })

    constructor(
        format: NeoVertexFormat,
        mode: GlBeginMode,
        numVertices: Int
    ) : this(format, mode, numVertices, NeoBuffer::Native, { it?.let { NeoBuffer.Native(it) } })

    override fun build(): Built? {
        end()

        if (filledVertices != numVertices) {
            return null
        }

        if (indexBuffer != null) {
            var putIndex = 0L
            var vertex = 0

            repeat(numVertices / mode.indexData!!.stride) {
                for (n in mode.indexData.offsets) {
                    indexType!!.put(indexBuffer, putIndex, n + vertex)
                    putIndex += indexType.sizeBytes
                }

                vertex += mode.indexData.stride
            }
        }

        return object : Built {
            override val vertexBuffer: NeoBuffer = this@KnownSizeNeoBufferBuilderImpl.vertexBuffer
            override val indexBuffer: NeoBuffer? = this@KnownSizeNeoBufferBuilderImpl.indexBuffer
            override val format: NeoVertexFormat = this@KnownSizeNeoBufferBuilderImpl.format
            override val vertexCount: Int = numVertices
            override val indexCount: Int? = numIndices
            override val mode: GlBeginMode = this@KnownSizeNeoBufferBuilderImpl.mode
            override val indexType: GlIndexDataType? = this@KnownSizeNeoBufferBuilderImpl.indexType

            override fun free() {
                this@KnownSizeNeoBufferBuilderImpl.vertexBuffer.free()
                this@KnownSizeNeoBufferBuilderImpl.indexBuffer?.free()
            }
        }
    }

    override fun put(element: NeoVertexFormat.Element, data: NeoBuffer.(index: Long) -> Unit) {
        val offset = format.getElementOffset(element)

        if (offset == -1) {
            return
        }

        val mask = 1 shl format.elements.indexOf(element)

        if (filledElements and mask != 0) {
            throw IllegalStateException("Already filled element $element")
        }

        filledElements = filledElements or mask
        data(vertexBuffer, currentIndex + offset)
    }

    override fun end() {
        if (filledElements == 0 && filledVertices == 0) {
            return
        }

        if (filledElements != (1 shl format.elements.size) - 1) {
            val missing = arrayListOf<NeoVertexFormat.Element>()

            format.elements.forEachIndexed { index, element ->
                if (filledElements and (1 shl index) == 0) {
                    missing.add(element)
                }
            }

            throw IllegalStateException("Missing vertex elements $missing for $format")
        }

        filledVertices++
        currentIndex += format.vertexSizeBytes
        filledElements = 0

        if (filledVertices > numVertices) {
            throw IllegalStateException("Overflowed known size neo buffer builder, expected $numVertices vertices")
        }
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