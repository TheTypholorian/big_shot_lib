package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import com.mojang.blaze3d.opengl.GlStateManager

enum class GlVertexElementReadType {
    INT_TO_INT {
        override fun setupBufferState(
            count: Int,
            type: Int,
            stride: Int,
            offset: Long,
            index: Int
        ) {
            GlStateManager._vertexAttribIPointer(index, count, type, stride, offset)
        }

        override fun supports(inType: GlDataType): Boolean {
            return inType != GlDataType.FLOAT
        }
    },
    INT_TO_FLOAT {
        override fun setupBufferState(
            count: Int,
            type: Int,
            stride: Int,
            offset: Long,
            index: Int
        ) {
            GlStateManager._vertexAttribPointer(index, count, type, true, stride, offset)
        }

        override fun supports(inType: GlDataType): Boolean {
            return inType != GlDataType.FLOAT
        }
    },
    FLOAT_TO_FLOAT {
        override fun setupBufferState(
            count: Int,
            type: Int,
            stride: Int,
            offset: Long,
            index: Int
        ) {
            GlStateManager._vertexAttribPointer(index, count, type, false, stride, offset)
        }

        override fun supports(inType: GlDataType): Boolean {
            return inType == GlDataType.FLOAT
        }
    };

    abstract fun setupBufferState(
        count: Int,
        type: Int,
        stride: Int,
        offset: Long,
        index: Int
    )

    abstract fun supports(inType: GlDataType): Boolean
}