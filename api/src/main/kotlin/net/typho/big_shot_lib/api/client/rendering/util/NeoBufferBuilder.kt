package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlIndexDataType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBufferWriter
import net.typho.big_shot_lib.api.util.BYTE_MASK
import net.typho.big_shot_lib.api.util.SHORT_MASK
import java.io.DataOutput
import java.io.DataOutputStream

open class NeoBufferBuilder(
    @JvmField
    val format: NeoVertexFormat,
    @JvmField
    val mode: GlBeginMode,
    @JvmField
    val numVertices: Int,
    vertexBuffer: (size: Long) -> GlBufferWriter,
    indexBuffer: (size: Long?) -> GlBufferWriter?
) : NeoVertexConsumer() {
    protected var filledVertices: Int = 0
    protected var filledElements: Int = 0
    protected var currentIndex: Long = 0

    protected val vertexBuffer = vertexBuffer(numVertices.toLong() * format.vertexSizeBytes)
    protected val numIndices: Int? = mode.indexData?.let {
        if (numVertices % it.stride != 0) {
            throw IllegalArgumentException("Vertex count $numVertices is not a multiple of ${it.stride} required for $mode")
        }

        numVertices / it.stride * it.offsets.size
    }
    protected val indexType: GlIndexDataType? = numIndices?.let {
        when (it) {
            it and BYTE_MASK -> GlIndexDataType.BYTE
            it and SHORT_MASK -> GlIndexDataType.SHORT
            else -> GlIndexDataType.INT
        }
    }
    protected val indexBuffer = indexBuffer(numIndices?.let { it.toLong() * indexType!!.sizeBytes })

    fun put(element: NeoVertexFormat.Element, data: DataOutput.() -> Unit) {
        val offset = format.getElementOffset(element)

        if (offset == -1) {
            return
        }

        val mask = 1 shl format.elements.indexOf(element)

        if (filledElements and mask != 0) {
            throw IllegalStateException("Already filled element $element")
        }

        filledElements = filledElements or mask
        data(vertexBuffer.write(currentIndex + offset))
    }

    protected fun end() {
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

    fun build(): Built? {
        end()

        if (filledVertices != numVertices) {
            return null
        }

        if (indexBuffer != null) {
            val indexOut = indexBuffer.write()
            var vertex = 0

            repeat(numVertices / mode.indexData!!.stride) {
                for (n in mode.indexData.offsets) {
                    indexType!!.write(indexOut, n + vertex)
                }

                vertex += mode.indexData.stride
            }
        }

        return Built(
            format,
            numVertices,
            numIndices,
            mode,
            indexType
        )
    }

    override fun vertex(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        end()
        put(NeoVertexFormat.Element.POSITION) {
            writeFloat(x)
            writeFloat(y)
            writeFloat(z)
        }
        return this
    }

    override fun color(
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.COLOR) {
            write(r)
            write(g)
            write(b)
            write(a)
        }
        return this
    }

    override fun textureUV(
        u: Float,
        v: Float
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.TEXTURE_UV) {
            writeFloat(u)
            writeFloat(v)
        }
        return this
    }

    override fun overlayUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.OVERLAY_UV) {
            writeShort(u)
            writeShort(v)
        }
        return this
    }

    override fun lightUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.LIGHT_UV) {
            writeShort(u)
            writeShort(v)
        }
        return this
    }

    override fun normal(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.NORMAL) {
            write((x * 127).toInt())
            write((y * 127).toInt())
            write((z * 127).toInt())
        }
        return this
    }

    override fun vertex(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        end()
        put(NeoVertexFormat.Element.POSITION) {
            writeInt(packed[offset])
            writeInt(packed[offset + 1])
            writeInt(packed[offset + 2])
        }
        return this
    }

    override fun color(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.COLOR) {
            writeInt(packed[offset])
        }
        return this
    }

    override fun color(argb: Int): NeoVertexConsumer {
        put(NeoVertexFormat.Element.COLOR) {
            writeInt(argb and 0xFFFFFF)
            write(argb ushr 24)
        }
        return this
    }

    override fun textureUV(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.TEXTURE_UV) {
            writeInt(packed[offset])
            writeInt(packed[offset + 1])
        }
        return this
    }

    override fun overlayUV(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.OVERLAY_UV) {
            writeInt(packed[offset])
        }
        return this
    }

    override fun overlayUV(packed: Int): NeoVertexConsumer {
        put(NeoVertexFormat.Element.OVERLAY_UV) {
            writeInt(packed)
        }
        return this
    }

    override fun lightUV(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.LIGHT_UV) {
            writeInt(packed[offset])
        }
        return this
    }

    override fun lightUV(packed: Int): NeoVertexConsumer {
        put(NeoVertexFormat.Element.LIGHT_UV) {
            writeInt(packed)
        }
        return this
    }

    override fun normal(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.NORMAL) {
            writeInt(packed[offset])
        }
        return this
    }

    override fun normal(packed: Int): NeoVertexConsumer {
        put(NeoVertexFormat.Element.NORMAL) {
            writeInt(packed)
        }
        return this
    }

    data class Built(
        @JvmField
        val format: NeoVertexFormat,
        @JvmField
        val vertexCount: Int,
        @JvmField
        val indexCount: Int?,
        @JvmField
        val mode: GlBeginMode,
        @JvmField
        val indexType: GlIndexDataType?
    )
}