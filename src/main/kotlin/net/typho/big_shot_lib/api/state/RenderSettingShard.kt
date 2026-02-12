package net.typho.big_shot_lib.api.state

import com.mojang.serialization.MapCodec
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.util.Bindable
import net.typho.big_shot_lib.api.util.Named

interface RenderSettingShard : Bindable {
    fun type(): Type<*>

    interface Type<S : RenderSettingShard> : Named {
        fun getDefault(): S

        fun codec(): MapCodec<S>?
    }

    open class Basic(
        @JvmField
        val type: Type<*>,
        @JvmField
        val list: List<Bindable>
    ) : RenderSettingShard {
        override fun bind() {
            list.forEach { it.bind() }
        }

        override fun unbind() {
            list.forEach { it.unbind() }
        }

        override fun type() = type
    }

    companion object {
        @JvmField
        val REGISTRY = HashMap<Identifier, Type<*>>()
        @JvmField
        val CODEC: MapCodec<RenderSettingShard> = Identifier.CODEC.dispatchMap(
            { shard -> shard.type().location() },
            { location -> REGISTRY[location]?.codec() }
        )

        init {
            REGISTRY[BlendShard.location()] = BlendShard
            REGISTRY[ColorMaskShard.location()] = ColorMaskShard
            REGISTRY[CullShard.location()] = CullShard
            REGISTRY[DepthMaskShard.location()] = DepthMaskShard
            REGISTRY[DepthTestShard.location()] = DepthTestShard
            REGISTRY[PolygonModeShard.location()] = PolygonModeShard
            REGISTRY[StencilShard.location()] = StencilShard
        }
    }
}