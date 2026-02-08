package net.typho.big_shot_lib.api.gl_state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.OpenGL
import net.typho.big_shot_lib.api.util.NeoCodecs

open class CullShard(
    @JvmField
    val enabled: Boolean,
    @JvmField
    val face: CullFace
) : RenderSettingShard.Basic(
    CullShard,
    if (enabled) listOf(
        GlFlag.CULL_FACE.bindable,
        Bindable.ofState(OpenGL::cullFace, face, CullFace.DEFAULT)
    ) else listOf()
) {
    companion object : RenderSettingShard.Type<CullShard> {
        override fun getDefault() = CullShard(
            false,
            CullFace.DEFAULT
        )

        override fun codec(): MapCodec<CullShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<CullFace>().fieldOf("face").forGetter { shard -> shard.face },
            ).apply(it) { face -> CullShard(true, face) }
        }

        override fun location(): ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "cull_face")
    }
}