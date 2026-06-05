package net.typho.big_shot_lib.api.client.rendering.state

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlLogicOp
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import java.util.function.Supplier

interface GpuDrawState {
    val blend: BlendFunction?
    val shader: Supplier<GlProgram>?
    val texture: TextureBinding?
    val lightmap: Boolean
    val overlay: Boolean
    val cull: Boolean
    val depth: GlAlphaFunction?
    val writeColor: Boolean
    val writeDepth: Boolean
    val colorLogic: GlLogicOp?
    val layering: LayeringState

    fun bind()

    fun unbind()

    open class Builder {
        constructor()

        constructor(state: GpuDrawState) {
            blend = state.blend
            shader = state.shader
            texture = state.texture
            lightmap = state.lightmap
            overlay = state.overlay
            cull = state.cull
            depth = state.depth
            writeColor = state.writeColor
            writeDepth = state.writeDepth
            colorLogic = state.colorLogic
            layering = state.layering
        }

        @JvmField
        var blend: BlendFunction? = null
        @JvmField
        var shader: Supplier<GlProgram>? = null
        @JvmField
        var texture: TextureBinding? = null
        @JvmField
        var lightmap: Boolean = false
        @JvmField
        var overlay: Boolean = false
        @JvmField
        var cull: Boolean = false
        @JvmField
        var depth: GlAlphaFunction? = null
        @JvmField
        var writeColor: Boolean = true
        @JvmField
        var writeDepth: Boolean = true
        @JvmField
        var colorLogic: GlLogicOp? = null
        @JvmField
        var layering: LayeringState = LayeringState.DISABLED

        @JvmOverloads
        fun blend(blend: BlendFunction? = BlendFunction.TRANSLUCENT): Builder {
            this.blend = blend
            return this
        }

        fun shader(shader: Supplier<GlProgram>?): Builder {
            this.shader = shader
            return this
        }

        fun shader(shader: Identifier) = shader { GlProgram.getOrThrow(shader) }

        fun texture(texture: TextureBinding?): Builder {
            this.texture = texture
            return this
        }

        @JvmOverloads
        fun texture(texture: Identifier, blur: Boolean = false, mipmap: Boolean = true) = texture(TextureBinding.FromLocation(texture, blur, mipmap))

        @JvmOverloads
        fun texture(texture: () -> GlTexture2D, blur: Boolean = false, mipmap: Boolean = true) = texture(TextureBinding.FromSupplier(texture, blur, mipmap))

        @JvmOverloads
        fun lightmap(lightmap: Boolean = true): Builder {
            this.lightmap = lightmap
            return this
        }

        @JvmOverloads
        fun overlay(overlay: Boolean = true): Builder {
            this.overlay = overlay
            return this
        }

        @JvmOverloads
        fun cull(cull: Boolean = true): Builder {
            this.cull = cull
            return this
        }

        @JvmOverloads
        fun depth(depth: GlAlphaFunction? = GlAlphaFunction.LEQUAL): Builder {
            this.depth = depth
            return this
        }

        @JvmOverloads
        fun writeMask(writeColor: Boolean = true, writeDepth: Boolean = true): Builder {
            this.writeColor = writeColor
            this.writeDepth = writeDepth
            return this
        }

        fun colorLogic(colorLogic: GlLogicOp): Builder {
            this.colorLogic = colorLogic
            return this
        }

        fun layering(layering: LayeringState): Builder {
            this.layering = layering
            return this
        }

        fun polygonOffsetLayering() = layering(LayeringState.POLYGON_OFFSET)

        fun viewOffsetLayering() = layering(LayeringState.VIEW_OFFSET)
    }
}