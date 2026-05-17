package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

interface GlDrawState {
    val blend: GlBlendShard
    val colorMask: GlColorMaskShard
    val cull: GlCullShard
    val depth: GlDepthShard
    val layering: GlLayeringShard
    val lightmap: GlLightmapShard
    val overlay: GlOverlayShard
    val shader: GlShaderShard

    fun bind(): Bound {
        val blend = blend.bind()
        val colorMask = colorMask.bind()
        val cull = cull.bind()
        val depth = depth.bind()
        val layering = this@GlDrawState.layering.bind()
        val shader = shader.bind()

        return object : Bound {
            override val parent: GlDrawState = this@GlDrawState
            override val shader: GlBoundProgram = shader

            override fun unbind() {
                blend.unbind()
                colorMask.unbind()
                cull.unbind()
                depth.unbind()
                layering.unbind()
                shader.unbind()
            }
        }
    }

    open class Builder(
        @JvmField
        var shader: GlShaderShard
    ) {
        @JvmField
        var blend: GlBlendShard = GlBlendShard.Disabled
        @JvmField
        var colorMask: GlColorMaskShard = GlColorMaskShard(true)
        @JvmField
        var cull: GlCullShard = GlCullShard.Disabled
        @JvmField
        var depth: GlDepthShard = GlDepthShard.Disabled
        @JvmField
        var layering: GlLayeringShard = GlLayeringShard.Disabled
        @JvmField
        var lightmap: GlLightmapShard = GlLightmapShard(false)
        @JvmField
        var overlay: GlOverlayShard = GlOverlayShard(false)

        fun blend(shard: GlBlendShard): Builder {
            blend = shard
            return this
        }

        fun colorMask(shard: GlColorMaskShard): Builder {
            colorMask = shard
            return this
        }

        fun cull(shard: GlCullShard): Builder {
            cull = shard
            return this
        }

        fun depth(shard: GlDepthShard): Builder {
            depth = shard
            return this
        }

        fun layering(shard: GlLayeringShard): Builder {
            layering = shard
            return this
        }

        fun lightmap(shard: GlLightmapShard): Builder {
            lightmap = shard
            return this
        }

        fun overlay(shard: GlOverlayShard): Builder {
            overlay = shard
            return this
        }

        fun shader(shard: GlShaderShard): Builder {
            shader = shard
            return this
        }

        fun build() = Basic(
            blend,
            colorMask,
            cull,
            depth,
            layering,
            lightmap,
            overlay,
            shader
        )
    }

    interface Bound : BoundResource {
        val parent: GlDrawState
        val shader: GlBoundProgram
    }

    open class Basic(
        override val blend: GlBlendShard = GlBlendShard.Disabled,
        override val colorMask: GlColorMaskShard = GlColorMaskShard(true),
        override val cull: GlCullShard = GlCullShard.Disabled,
        override val depth: GlDepthShard = GlDepthShard.Disabled,
        override val layering: GlLayeringShard = GlLayeringShard.Disabled,
        override val lightmap: GlLightmapShard = GlLightmapShard(false),
        override val overlay: GlOverlayShard = GlOverlayShard(false),
        override val shader: GlShaderShard
    ) : GlDrawState
}