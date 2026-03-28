package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlPolygonMode
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
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

    fun bind(): Bound {
        val blend = blend.bind()
        val colorMask = colorMask.bind()
        val cull = cull.bind()
        val depth = depth.bind()
        val polygonMode = polygonMode.bind()
        val polygonOffset = polygonOffset.bind()
        val scissor = scissor.bind()
        val shader = shader.bind()
        val stencil = stencil.bind()

        return object : Bound {
            override val parent: GlDrawState = this@GlDrawState
            override val shader: GlBoundProgram = shader

            override fun unbind() {
                blend.unbind()
                colorMask.unbind()
                cull.unbind()
                depth.unbind()
                polygonMode.unbind()
                polygonOffset.unbind()
                scissor.unbind()
                shader.unbind()
                stencil.unbind()
            }
        }
    }

    interface Bound : BoundResource {
        val parent: GlDrawState
        val shader: GlBoundProgram
    }

    open class Basic(
        override val blend: GlBlendShard = GlBlendShard.Disabled,
        override val colorMask: GlColorMaskShard = GlColorMaskShard(ColorMask.DEFAULT),
        override val cull: GlCullShard = GlCullShard.Disabled,
        override val depth: GlDepthShard = GlDepthShard.Disabled,
        override val polygonMode: GlPolygonModeShard = GlPolygonModeShard(GlPolygonMode.FILL),
        override val polygonOffset: GlPolygonOffsetShard = GlPolygonOffsetShard.Disabled,
        override val scissor: GlScissorShard = GlScissorShard.Disabled,
        override val shader: GlShaderShard,
        override val stencil: GlStencilShard = GlStencilShard.Disabled
    ) : GlDrawState
}