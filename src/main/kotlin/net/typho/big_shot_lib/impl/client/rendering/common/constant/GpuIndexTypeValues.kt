package net.typho.big_shot_lib.impl.client.rendering.common.constant

import com.mojang.blaze3d.IndexType
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuIndexType
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import net.typho.big_shot_lib.api.util.resource.NeoCodecs

object GpuIndexTypeValues : GpuIndexType.Values {
    override val codec: Codec<GpuIndexType> = NeoCodecs.enumCodec<IndexType>().xmap({ it }, { it.castTo() })
    override val short: GpuIndexType = IndexType.SHORT
    override val int: GpuIndexType = IndexType.INT
}