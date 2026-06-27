package net.typho.big_shot_lib.api.client.ext

import com.mojang.blaze3d.buffers.GpuBufferSlice
import net.typho.big_shot_lib.api.client.rendering.common.GpuBuffer
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuIndexType
import net.typho.big_shot_lib.api.util.Extension

interface RenderPassExtension : Extension<Any> {
    fun setUniform(name: String, buffer: GpuBuffer) {
        throw UnsupportedOperationException("Implemented via mixin")
    }

    fun setStorageBuffer(index: Int, buffer: GpuBuffer) {
        throw UnsupportedOperationException("Implemented via mixin")
    }

    fun setStorageBuffer(index: Int, buffer: GpuBufferSlice) {
        throw UnsupportedOperationException("Implemented via mixin")
    }

    fun setIndexBuffer(buffer: GpuBuffer, type: GpuIndexType) {
        throw UnsupportedOperationException("Implemented via mixin")
    }
}