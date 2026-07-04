package net.typho.big_shot_lib.api.client.ext

import net.typho.big_shot_lib.api.client.rendering.common.GpuBuffer
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuIndexType
import net.typho.big_shot_lib.api.util.Extension

interface RenderPassExtension : Extension<Any> {
    fun setUniform(name: String, buffer: GpuBuffer) {
        throw UnsupportedOperationException("Implemented via mixin $this")
    }

    fun setIndexBuffer(buffer: GpuBuffer, type: GpuIndexType) {
        throw UnsupportedOperationException("Implemented via mixin $this")
    }
}