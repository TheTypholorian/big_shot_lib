package net.typho.big_shot_lib.state

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.state.gl.*

interface IRenderSetting {
    fun location(): ResourceLocation

    fun push()

    fun pop()

    companion object {
        @JvmField
        val REGISTRY = HashMap<ResourceLocation, MapCodec<out IRenderSetting>>()
        @JvmField
        val CODEC: MapCodec<IRenderSetting> = ResourceLocation.CODEC.dispatchMap(
            { shard -> shard.location() },
            { key -> REGISTRY[key] }
        )

        private data class Impl1<S : GlState<S>>(val location: ResourceLocation, val state: S) : IRenderSetting {
            override fun location() = location

            override fun push() {
                state.set(state)
            }

            override fun pop() {
                state.set(state.default())
            }
        }

        private data class Impl2<S : GlState<T>, T>(val location: ResourceLocation, val state: S, val value: T) : IRenderSetting {
            override fun location() = location

            override fun push() {
                state.set(value)
            }

            override fun pop() {
                state.set(state.default())
            }
        }

        fun <S : GlState<S>> register(location: ResourceLocation, codec: MapCodec<S>) {
            REGISTRY[location] = codec.xmap(
                { state -> Impl1(location, state) },
                { impl -> impl.state }
            )
        }

        fun <S : GlState<T>, T> register(state: S, location: ResourceLocation, codec: MapCodec<T>) {
            REGISTRY[location] = codec.xmap(
                { value -> Impl2(location, state, value) },
                { impl -> impl.value }
            )
        }

        init {
            register(BlendColor.LOCATION, BlendColor.CODEC)
            register(BlendEquation.LOCATION, BlendEquation.CODEC.fieldOf("equation"))
            register(BlendFunction.LOCATION, BlendFunction.CODEC)
            register(ColorMask.LOCATION, ColorMask.CODEC)
            register(CullFace.LOCATION, CullFace.CODEC.fieldOf("face"))
            register(DepthMask, DepthMask.LOCATION, DepthMask.CODEC.fieldOf("mask"))
            register(DepthTest, DepthTest.LOCATION, DepthTest.CODEC.fieldOf("mode"))

            for (flag in GlFlag.entries) {
                if (flag.hasShard) {
                    register(flag, flag.location, Codec.BOOL.fieldOf("enabled"))
                }
            }
        }
    }
}