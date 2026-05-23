package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlLogicOp
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.plugin.Namespace
import java.util.function.Supplier

@Namespace(BigShotApi.MOD_ID)
interface GlDrawState {
    val blend: BlendFunction?
    val shader: Supplier<GlProgram>?
    val texture: GlTextureBinding?
    val lightmap: Boolean
    val overlay: Boolean
    val cull: Boolean
    val depth: GlAlphaFunction?
    val writeColor: Boolean
    val writeDepth: Boolean
    val colorLogic: GlLogicOp?
    val layering: LayeringState

    /*
    val blend: GlBlendShard
    val colorMask: GlColorMaskShard
    val cull: GlCullShard
    val depth: GlDepthShard
    val layering: GlLayeringShard
    val lightmap: GlLightmapShard
    val overlay: GlOverlayShard
    val shader: GlShaderShard
     */

    fun bind(): BoundResource

    open class Builder {
        @JvmField
        var blend: BlendFunction? = null
        @JvmField
        var shader: Supplier<GlProgram>? = null
        @JvmField
        var texture: GlTextureBinding? = null
        @JvmField
        var lightmap: Boolean = false
        @JvmField
        var overlay: Boolean = false
        @JvmField
        var cull: Boolean = false
        @JvmField
        var depth: GlAlphaFunction? = null
        @JvmField
        var writeColor: Boolean = false
        @JvmField
        var writeDepth: Boolean = false
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

        fun shader(shader: Identifier) = shader { GlProgram[shader] }

        fun texture(texture: GlTextureBinding?): Builder {
            this.texture = texture
            return this
        }

        @JvmOverloads
        fun texture(texture: Identifier, blur: Boolean = false, mipmap: Boolean = true) = texture(GlTextureBinding.FromLocation(texture, blur, mipmap))

        @JvmOverloads
        fun texture(texture: () -> GlTexture2D, blur: Boolean = false, mipmap: Boolean = true) = texture(GlTextureBinding.FromSupplier(texture, blur, mipmap))

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