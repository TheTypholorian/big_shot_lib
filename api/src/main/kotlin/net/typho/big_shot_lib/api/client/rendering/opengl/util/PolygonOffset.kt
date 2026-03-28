package net.typho.big_shot_lib.api.client.rendering.opengl.util

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class PolygonOffset(
    @JvmField
    val factor: Float,
    @JvmField
    val units: Float
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<PolygonOffset> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.FLOAT.fieldOf("factor").forGetter { offset -> offset.factor },
                Codec.FLOAT.fieldOf("units").forGetter { offset -> offset.units }
            ).apply(it, ::PolygonOffset)
        }
    }
}
