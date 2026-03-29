package net.typho.big_shot_lib.impl.client.rendering.util

//? if <1.21 {
import com.google.common.collect.ImmutableMap
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import net.typho.big_shot_lib.impl.mixin.VertexFormatAccessor
//? }

import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlDataType

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
        //? if >=1.21 {
        /*get() = inner.offsetsByElement
        *///? } else {
        get() = (inner as VertexFormatAccessor).`big_shot_lib$getOffsets`().toIntArray()
        //? }

    override fun getElementName(element: NeoVertexFormat.Element): String {
        //? if >=1.21 {
        /*return inner.getElementName((element as ElementImpl).inner)
        *///? } else {
        return (inner as VertexFormatAccessor).`big_shot_lib$getElementMapping`().entries.first { entry -> entry.value == (element as ElementImpl).inner }.key
        //? }
    }

    override fun getElementOffset(element: NeoVertexFormat.Element): Int {
        return elementOffsets[elements.indexOf(element)]
    }

    override fun initVertexArrayState() {
        //? if <1.21.5 {
        inner.setupBufferState()
        //? } else {
        /*elements.forEachIndexed { index, element ->
            element.vertexAttribPointer(index, getElementOffset(element).toLong(), vertexSizeBytes)
        }
        *///? }
    }

    data class ElementImpl(
        @JvmField
        val inner: VertexFormatElement
    ) : NeoVertexFormat.Element {
        override val index: Int
            get() = inner.index
        override val type: GlDataType
            get() = when (inner.type) {
                VertexFormatElement.Type.FLOAT -> GlDataType.FLOAT
                VertexFormatElement.Type.UBYTE -> GlDataType.UNSIGNED_BYTE
                VertexFormatElement.Type.BYTE -> GlDataType.BYTE
                VertexFormatElement.Type.USHORT -> GlDataType.UNSIGNED_SHORT
                VertexFormatElement.Type.SHORT -> GlDataType.SHORT
                VertexFormatElement.Type.UINT -> GlDataType.UNSIGNED_INT
                VertexFormatElement.Type.INT -> GlDataType.INT
            }
        override val normalized: Boolean?
            get() = when (inner.usage) {
                VertexFormatElement.Usage.POSITION -> false
                VertexFormatElement.Usage.NORMAL -> true
                VertexFormatElement.Usage.COLOR -> false
                VertexFormatElement.Usage.UV -> if (inner.type == VertexFormatElement.Type.FLOAT) false else null
                VertexFormatElement.Usage.GENERIC -> false
                //? if <1.21 {
                VertexFormatElement.Usage.PADDING -> null
                //? }
            }
        override val count: Int
            get() = inner.count
        override val sizeBytes: Int
            //? if >=1.21 {
            /*get() = inner.byteSize()
            *///? } else {
            get() = inner.byteSize
            //? }

        override fun vertexAttribPointer(index: Int, offset: Long, stride: Int) {
            type.vertexAttribPointer(index, count, normalized, stride, offset)
        }
    }

    //? if >=1.21 {
    /*data class BuilderImpl(
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
    *///? } else {
    class BuilderImpl : NeoVertexFormat.Builder {
        private var paddingIndex = 0
        @JvmField
        val builder = ImmutableMap.Builder<String, VertexFormatElement>()

        override fun add(
            name: String,
            element: NeoVertexFormat.Element
        ): NeoVertexFormat.Builder {
            builder.put(name, (element as ElementImpl).inner)
            return this
        }

        override fun padding(bytes: Int): NeoVertexFormat.Builder {
            repeat(bytes) { builder.put("Padding${paddingIndex++}", DefaultVertexFormat.ELEMENT_PADDING) }
            return this
        }

        override fun build(): NeoVertexFormat {
            return NeoVertexFormatImpl(VertexFormat(builder.build()))
        }
    }
    //? }
}