package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendEquation
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlCullFace
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlSampler
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.PolygonOffset
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

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

    open class Builder {
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
        var shader: GlShaderShard = GlShaderShard.Disabled
        @JvmField
        var overlay: GlOverlayShard = GlOverlayShard(false)

        fun blend(shard: GlBlendShard): Builder {
            blend = shard
            return this
        }

        @JvmOverloads
        fun blend(
            function: BlendFunction,
            equation: GlBlendEquation = GlBlendEquation.ADD,
            color: NeoColor? = null
        ) = blend(GlBlendShard.Enabled(function, equation, color))

        fun colorMask(shard: GlColorMaskShard): Builder {
            colorMask = shard
            return this
        }

        fun colorMask(
            mask: Boolean
        ) = colorMask(GlColorMaskShard(mask))

        fun cull(shard: GlCullShard): Builder {
            cull = shard
            return this
        }

        @JvmOverloads
        fun cull(
            face: GlCullFace = GlCullFace.BACK
        ) = cull(GlCullShard.Enabled(face))

        fun depth(shard: GlDepthShard): Builder {
            depth = shard
            return this
        }

        @JvmOverloads
        fun depth(
            face: GlAlphaFunction = GlAlphaFunction.LEQUAL
        ) = depth(GlDepthShard.Enabled(face))

        fun layering(shard: GlLayeringShard): Builder {
            layering = shard
            return this
        }

        fun layering(
            offset: PolygonOffset
        ) = layering(GlLayeringShard.EnabledPolygonOffset(offset))

        fun polygonOffsetLayering() = layering(PolygonOffset(-1f, -10f))

        fun layering(
            scale: IVec3<Float>
        ) = layering(GlLayeringShard.EnabledViewOffset(scale))

        fun viewOffsetLayering() = layering(NeoVec3f(0.99975586f, 0.99975586f, 0.99975586f))

        fun lightmap(shard: GlLightmapShard): Builder {
            lightmap = shard
            return this
        }

        @JvmOverloads
        fun lightmap(
            enabled: Boolean = true
        ) = lightmap(GlLightmapShard(enabled))

        fun overlay(shard: GlOverlayShard): Builder {
            overlay = shard
            return this
        }

        @JvmOverloads
        fun overlay(
            enabled: Boolean = true
        ) = overlay(GlOverlayShard(enabled))

        fun shader(shard: GlShaderShard): Builder {
            shader = shard
            return this
        }

        @JvmOverloads
        fun shader(
            location: NeoIdentifier,
            uniforms: Consumer<GlBoundProgram> = { }
        ) = shader(GlShaderShard.FromLocation(location, uniforms))

        @JvmOverloads
        fun shader(
            getter: () -> GlProgram?,
            uniforms: Consumer<GlBoundProgram> = { }
        ) = shader(GlShaderShard.FromInstance(getter, uniforms))

        fun texture(name: String, binding: GlTextureBinding): Builder {
            (shader.textures as MutableMap<String, GlTextureBinding>)[name] = binding
            return this
        }

        @JvmOverloads
        fun texture(name: String, location: NeoIdentifier, target: GlTextureTarget = GlTextureTarget.TEXTURE_2D, sampler: GlSampler? = null) = texture(name, GlTextureBinding.FromLocation(location, target, sampler))

        @JvmOverloads
        fun texture(name: String, texture: () -> GlTexture2D, target: GlTextureTarget = GlTextureTarget.TEXTURE_2D, sampler: GlSampler? = null) = texture(name, GlTextureBinding.FromInstance(texture, target, sampler))

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