package net.typho.big_shot_lib.impl.client.rendering.common.constant

import com.mojang.blaze3d.GpuFormat
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuTextureFormat
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import net.typho.big_shot_lib.api.util.resource.NeoCodecs

object GpuTextureFormatValues : GpuTextureFormat.Values {
    override val codec: Codec<GpuTextureFormat> = NeoCodecs.enumCodec<GpuFormat>().xmap({ it }, { it.castTo() })
    override val rgba: GpuTextureFormat = GpuFormat.RGBA8_UNORM
    override val rgb: GpuTextureFormat = GpuFormat.RGB8_UNORM
}