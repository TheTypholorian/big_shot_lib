package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendEquation
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.util.NeoColor

sealed interface GlBlendShard : GlDrawStateShard {
    data class Enabled @JvmOverloads constructor(
        @JvmField
        val function: BlendFunction,
        @JvmField
        val equation: GlBlendEquation = GlBlendEquation.ADD,
        @JvmField
        val color: NeoColor? = null
    ) : GlBlendShard {
        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.MAIN.blendEnabled.push(true)

            val function = NeoGlStateManager.MAIN.blendFunction.push(function)
            val equation = NeoGlStateManager.MAIN.blendEquation.push(equation)
            val color = color?.let { NeoGlStateManager.MAIN.blendColor.push(it) }

            return BoundResource.all(flag, function, equation, color)
        }
    }

    object Disabled : GlBlendShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.MAIN.blendEnabled.push(false)
        }
    }
}