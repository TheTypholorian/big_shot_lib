package net.typho.big_shot_lib.impl.meshes

import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexFormat
import net.typho.big_shot_lib.api.client.opengl.util.GlPrimitiveType

@JvmRecord
data class NeoVertexFormatImpl(
    @JvmField
    val inner: VertexFormat
) : NeoVertexFormat {
    override val vertexSizeBytes: Int
        get() = inner.vertexSize
    override val elements: Array<NeoVertexFormat.Element>
        get() = inner.elements.map { ElementImpl(it) }.toTypedArray()
    override val elementNames: Array<String>
        get() = inner.elementAttributeNames.toTypedArray()
    override val elementOffsets: IntArray
        get() = inner.offsetsByElement

    override fun getElementName(element: NeoVertexFormat.Element): String {
        return inner.getElementName((element as ElementImpl).inner)
    }

    override fun getElementOffset(element: NeoVertexFormat.Element): Int {
        return inner.getOffset((element as ElementImpl).inner)
    }

    override fun initVertexArrayState() {
        inner.setupBufferState()
    }

    @JvmRecord
    data class ElementImpl(
        @JvmField
        val inner: VertexFormatElement
    ) : NeoVertexFormat.Element {
        override val id: Int
            get() = inner.id
        override val index: Int
            get() = inner.index
        override val type: GlPrimitiveType
            get() = when (inner.type) {
                VertexFormatElement.Type.FLOAT -> GlPrimitiveType.FLOAT
                VertexFormatElement.Type.UBYTE -> GlPrimitiveType.UBYTE
                VertexFormatElement.Type.BYTE -> GlPrimitiveType.BYTE
                VertexFormatElement.Type.USHORT -> GlPrimitiveType.USHORT
                VertexFormatElement.Type.SHORT -> GlPrimitiveType.SHORT
                VertexFormatElement.Type.UINT -> GlPrimitiveType.UINT
                VertexFormatElement.Type.INT -> GlPrimitiveType.INT
            }
        override val normalized: Boolean?
            get() = when (inner.usage) {
                VertexFormatElement.Usage.POSITION -> false
                VertexFormatElement.Usage.NORMAL -> true
                VertexFormatElement.Usage.COLOR -> false
                VertexFormatElement.Usage.UV -> if (inner.type == VertexFormatElement.Type.FLOAT) false else null
                VertexFormatElement.Usage.GENERIC -> false
            }
        override val count: Int
            get() = inner.count
        override val mask: Int
            get() = inner.mask()
        override val sizeBytes: Int
            get() = inner.byteSize()

        override fun vertexAttribPointer(index: Int, offset: Long, stride: Int) {
            type.vertexAttribPointer(index, count, normalized, stride, offset)
        }
    }

    @JvmRecord
    data class BuilderImpl(
        @JvmField
        val inner: VertexFormat.Builder = VertexFormat.builder()
    ) : NeoVertexFormat.Builder {
        override fun add(
            name: String,
            element: NeoVertexFormat.Element
        ): NeoVertexFormat.Builder {
            inner.add(name, (element as ElementImpl).inner)
            return this
        }

        override fun padding(bytes: Int): NeoVertexFormat.Builder {
            inner.padding(bytes)
            return this
        }

        override fun build(): NeoVertexFormat {
            return NeoVertexFormatImpl(inner.build())
        }
    }
}