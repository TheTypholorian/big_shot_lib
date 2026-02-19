package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.opengl.GlConst
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.typho.big_shot_lib.api.client.rendering.services.GlUtil
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL

class GlUtilImpl : GlUtil {
    override fun toGlId(mode: VertexFormat.Mode): Int {
        return GlConst.toGl(mode)
    }

    override fun toGlId(type: VertexFormat.IndexType): Int {
        return GlConst.toGl(type)
    }

    override fun initBufferState(format: VertexFormat) {
        val stride = format.vertexSize//if (format.elements.size == 1) 12 else 20

        //println("Init buffer state $stride ${GlStateStack.buffers[BufferType.ELEMENT_ARRAY_BUFFER]!!.query.get()} ${GlStateStack.buffers[BufferType.ARRAY_BUFFER]!!.query.get()} ${GlStateStack.vertexArray.query.get()}")

        format.elements.forEachIndexed { i, element ->
            when (element.usage) {
                VertexFormatElement.Usage.GENERIC,
                VertexFormatElement.Usage.POSITION
                    -> OpenGL.INSTANCE.vertexAttribPointer(i, element.count, GlConst.toGl(element.type), false, stride, format.getOffset(element).toLong())
                VertexFormatElement.Usage.NORMAL,
                VertexFormatElement.Usage.COLOR,
                    -> OpenGL.INSTANCE.vertexAttribPointer(i, element.count, GlConst.toGl(element.type), true, stride, format.getOffset(element).toLong())
                VertexFormatElement.Usage.UV -> if (element.type == VertexFormatElement.Type.FLOAT) {
                    OpenGL.INSTANCE.vertexAttribPointer(i, element.count, GlConst.toGl(element.type), false, stride, format.getOffset(element).toLong())
                } else {
                    OpenGL.INSTANCE.vertexAttribIPointer(i, element.count, GlConst.toGl(element.type), stride, format.getOffset(element).toLong())
                }
            }

            OpenGL.INSTANCE.enableVertexAttribArray(i)

            //glVertexAttribBinding(i, 0)
        }
    }
}