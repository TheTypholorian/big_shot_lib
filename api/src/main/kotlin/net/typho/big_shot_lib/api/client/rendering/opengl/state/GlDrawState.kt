package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlPolygonMode
import net.typho.big_shot_lib.api.client.rendering.opengl.util.ColorMask
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

interface GlDrawState {
    val blend: GlBlendShard
    val colorMask: GlColorMaskShard
    val cull: GlCullShard
    val depth: GlDepthShard
    val polygonMode: GlPolygonModeShard
    val polygonOffset: GlPolygonOffsetShard
    val scissor: GlScissorShard
    val shader: GlShaderShard
    val stencil: GlStencilShard

    fun bind(): BoundResource {
        return BoundResource.all(
            blend::bind,
            colorMask::bind,
            cull::bind,
            depth::bind,
            polygonMode::bind,
            polygonOffset::bind,
            scissor::bind,
            shader::bind,
            stencil::bind
        )
    }

    companion object {
        @JvmStatic
        fun of(
            blend: GlBlendShard = GlBlendShard.Disabled,
            colorMask: GlColorMaskShard = GlColorMaskShard.of(ColorMask.DEFAULT),
            cull: GlCullShard = GlCullShard.Disabled,
            depth: GlDepthShard = GlDepthShard.Disabled,
            polygonMode: GlPolygonModeShard = GlPolygonModeShard.of(GlPolygonMode.FILL),
            polygonOffset: GlPolygonOffsetShard = GlPolygonOffsetShard.Disabled,
            scissor: GlScissorShard = GlScissorShard.Disabled,
            shader: GlShaderShard,
            stencil: GlStencilShard = GlStencilShard.Disabled,
        ) = object : GlDrawState {
            override val blend: GlBlendShard = blend
            override val colorMask: GlColorMaskShard = colorMask
            override val cull: GlCullShard = cull
            override val depth: GlDepthShard = depth
            override val polygonMode: GlPolygonModeShard = polygonMode
            override val polygonOffset: GlPolygonOffsetShard = polygonOffset
            override val scissor: GlScissorShard = scissor
            override val shader: GlShaderShard = shader
            override val stencil: GlStencilShard = stencil
        }
    }
}