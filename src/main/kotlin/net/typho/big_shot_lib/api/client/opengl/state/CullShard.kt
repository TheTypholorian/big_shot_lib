package net.typho.big_shot_lib.api.client.opengl.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.opengl.state.shards.RenderSettingShard
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.NeoCodecs
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class CullShard(
    @JvmField
    val enabled: Boolean,
    @JvmField
    val face: CullFace
) : RenderSettingShard.Basic(
    CullShard,
    if (enabled) listOf(
        GlBindable.ofStack(GlFlag.CULL_FACE.stack, true),
        GlBindable.ofStack(GlStateManager.cullFace, face)
    ) else listOf(GlBindable.ofStack(GlFlag.CULL_FACE.stack, false))
) {
    companion object : RenderSettingShard.Type<CullShard> {
        override val default = CullShard(
            false,
            CullFace.DEFAULT
        )
        override val codec: MapCodec<CullShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<CullFace>().fieldOf("face").forGetter { shard -> shard.face },
            ).apply(it) { face -> CullShard(true, face) }
        }
        override val location = ResourceIdentifier("opengl", "cull_face")
    }
}