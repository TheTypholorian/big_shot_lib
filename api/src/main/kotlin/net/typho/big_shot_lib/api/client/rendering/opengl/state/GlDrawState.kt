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
        return BoundResource.allGet(
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

    open class Basic(
        override val blend: GlBlendShard = GlBlendShard.Disabled,
        override val colorMask: GlColorMaskShard = GlColorMaskShard.of(ColorMask.DEFAULT),
        override val cull: GlCullShard = GlCullShard.Disabled,
        override val depth: GlDepthShard = GlDepthShard.Disabled,
        override val polygonMode: GlPolygonModeShard = GlPolygonModeShard.of(GlPolygonMode.FILL),
        override val polygonOffset: GlPolygonOffsetShard = GlPolygonOffsetShard.Disabled,
        override val scissor: GlScissorShard = GlScissorShard.Disabled,
        override val shader: GlShaderShard,
        override val stencil: GlStencilShard = GlStencilShard.Disabled
    ) : GlDrawState
}