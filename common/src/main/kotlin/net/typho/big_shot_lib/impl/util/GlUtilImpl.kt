package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.util.GlUtil

class GlUtilImpl : GlUtil {
    override fun toGlId(mode: VertexFormat.Mode): Int {
        return mode.asGLMode
    }

    override fun toGlId(type: VertexFormat.IndexType): Int {
        return type.asGLType
    }

    override fun initBufferState(format: VertexFormat) {
        format.setupBufferState()
    }
}