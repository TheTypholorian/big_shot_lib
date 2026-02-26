package net.typho.big_shot_lib.api.client.opengl.state

import com.mojang.serialization.MapCodec
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface RenderSettingShard : GlBindable {
    fun type(): Type<*>

    interface Type<S : RenderSettingShard> : NamedResource {
        fun getDefault(): S

        fun codec(): MapCodec<S>?
    }

    open class Basic(
        @JvmField
        val type: Type<*>,
        @JvmField
        val list: List<GlBindable>
    ) : RenderSettingShard {
        override fun bind(pushStack: Boolean) {
            list.forEach { it.bind(pushStack) }
        }

        override fun unbind(popStack: Boolean) {
            list.forEach { it.unbind(popStack) }
        }

        override fun type() = type
    }

    companion object {
        @JvmField
        val REGISTRY = HashMap<ResourceIdentifier, Type<*>>()
        @JvmField
        val CODEC: MapCodec<RenderSettingShard> = ResourceIdentifier.CODEC.dispatchMap(
            { shard -> shard.type().location },
            { location -> REGISTRY[location]?.codec() }
        )

        init {
            REGISTRY[BlendShard.location] = BlendShard
            REGISTRY[ColorMaskShard.location] = ColorMaskShard
            REGISTRY[CullShard.location] = CullShard
            REGISTRY[DepthMaskShard.location] = DepthMaskShard
            REGISTRY[DepthTestShard.location] = DepthTestShard
            REGISTRY[PolygonModeShard.location] = PolygonModeShard
            REGISTRY[StencilShard.location] = StencilShard.Companion
        }
    }
}