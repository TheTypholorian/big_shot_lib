package net.typho.big_shot_lib.api.client.opengl.state.shards

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.opengl.state.GlFlag
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.state.PolygonOffset
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class PolygonOffsetShard(
    @JvmField
    val offset: PolygonOffset
) : RenderSettingShard.Basic(
    PolygonOffsetShard,
    listOf(
        GlBindable.ofStack(GlFlag.POLYGON_OFFSET.stack, true),
        GlBindable.ofStack(GlStateStack.polygonOffset, offset)
    )
) {
    companion object : RenderSettingShard.Type<PolygonOffsetShard> {
        override val default = PolygonOffsetShard(PolygonOffset(0f, 0f))
        override val codec: MapCodec<PolygonOffsetShard> = RecordCodecBuilder.mapCodec {
            it.group(
                PolygonOffset.CODEC.fieldOf("offset").forGetter { shard -> shard.offset }
            ).apply(it, ::PolygonOffsetShard)
        }
        override val location = ResourceIdentifier("opengl", "polygon_offset")
    }
}