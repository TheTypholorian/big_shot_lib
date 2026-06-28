package net.typho.big_shot_lib.impl.client.rendering.common.constant

import com.mojang.blaze3d.GpuFormat
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuDataType
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import net.typho.big_shot_lib.api.util.resource.NeoCodecs

object GpuDataTypeValues : GpuDataType.Values {
    override val codec: Codec<GpuDataType> = NeoCodecs.enumCodec<GpuFormat.ComponentType>().xmap({ it }, { it.castTo() })
    override val unorm8: GpuDataType = GpuFormat.ComponentType.UNORM_8
    override val snorm8: GpuDataType = GpuFormat.ComponentType.SNORM_8
    override val uint8: GpuDataType = GpuFormat.ComponentType.UINT_8
    override val sint8: GpuDataType = GpuFormat.ComponentType.SINT_8
    override val unorm16: GpuDataType = GpuFormat.ComponentType.UNORM_16
    override val snorm16: GpuDataType = GpuFormat.ComponentType.SNORM_16
    override val uint16: GpuDataType = GpuFormat.ComponentType.UINT_16
    override val sint16: GpuDataType = GpuFormat.ComponentType.SINT_16
    override val float16: GpuDataType = GpuFormat.ComponentType.FLOAT_16
    override val uint32: GpuDataType = GpuFormat.ComponentType.UINT_32
    override val sint32: GpuDataType = GpuFormat.ComponentType.SINT_32
    override val float32: GpuDataType = GpuFormat.ComponentType.FLOAT_32
    override val opaque8: GpuDataType = GpuFormat.ComponentType.OPAQUE_8
    override val opaque16: GpuDataType = GpuFormat.ComponentType.OPAQUE_16
    override val opaque32: GpuDataType = GpuFormat.ComponentType.OPAQUE_32
    override val opaque64: GpuDataType = GpuFormat.ComponentType.OPAQUE_64
}