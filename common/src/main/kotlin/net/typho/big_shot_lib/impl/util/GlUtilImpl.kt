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
        format.elements.forEachIndexed { i, element ->
            OpenGL.INSTANCE.enableVertexAttribArray(i)

            when (element.usage) {
                VertexFormatElement.Usage.GENERIC,
                VertexFormatElement.Usage.POSITION
                    -> OpenGL.INSTANCE.vertexAttribPointer(i, element.count, GlConst.toGl(element.type), false, format.getOffset(element), 0L)
                VertexFormatElement.Usage.NORMAL,
                VertexFormatElement.Usage.COLOR,
                    -> OpenGL.INSTANCE.vertexAttribPointer(i, element.count, GlConst.toGl(element.type), true, format.getOffset(element), 0L)
                VertexFormatElement.Usage.UV -> if (element.type == VertexFormatElement.Type.FLOAT) {
                    OpenGL.INSTANCE.vertexAttribPointer(i, element.count, GlConst.toGl(element.type), false, format.getOffset(element), 0L)
                } else {
                    OpenGL.INSTANCE.vertexAttribIPointer(i, element.count, GlConst.toGl(element.type), format.getOffset(element), 0L)
                }
            }
        }
    }
}