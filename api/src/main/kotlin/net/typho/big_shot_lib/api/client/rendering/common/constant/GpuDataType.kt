package net.typho.big_shot_lib.api.client.rendering.common.constant

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import org.jetbrains.annotations.ApiStatus

private val INSTANCE by lazy { GpuDataType.Values::class.loadService() }

@ApiStatus.NonExtendable
interface GpuDataType : Extension<Any> {
    companion object : Values by INSTANCE

    interface Values {
        val codec: Codec<GpuDataType>

        val unorm8: GpuDataType
        val snorm8: GpuDataType
        val uint8: GpuDataType
        val sint8: GpuDataType
        val unorm16: GpuDataType
        val snorm16: GpuDataType
        val uint16: GpuDataType
        val sint16: GpuDataType
        val float16: GpuDataType
        val uint32: GpuDataType
        val sint32: GpuDataType
        val float32: GpuDataType
        val opaque8: GpuDataType
        val opaque16: GpuDataType
        val opaque32: GpuDataType
        val opaque64: GpuDataType
    }
}