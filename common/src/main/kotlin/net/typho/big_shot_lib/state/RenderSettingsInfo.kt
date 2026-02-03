package net.typho.big_shot_lib.state

import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.renderer.RenderType
import net.typho.big_shot_lib.util.NeoCodecs

data class RenderSettingsInfo(
    val bufferSize: Int,
    val format: VertexFormat,
    val mode: VertexFormat.Mode,
    val sort: Boolean,
    val shards: List<IRenderSetting>
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<RenderSettingsInfo> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.INT.optionalFieldOf("bufferSize", RenderType.TRANSIENT_BUFFER_SIZE)
                    .forGetter { settings -> settings.bufferSize },
                NeoCodecs.VERTEX_FORMAT_CODEC.fieldOf("format")
                    .forGetter { settings -> settings.format },
                NeoCodecs.VERTEX_FORMAT_MODE_CODEC.fieldOf("format")
                    .forGetter { settings -> settings.mode },
                Codec.BOOL.optionalFieldOf("sort", false)
                    .forGetter { settings -> settings.sort },
                IRenderSetting.CODEC.codec().listOf().optionalFieldOf("shards", listOf())
                    .forGetter { settings -> settings.shards }
            ).apply(it, ::RenderSettingsInfo)
        }
    }
}