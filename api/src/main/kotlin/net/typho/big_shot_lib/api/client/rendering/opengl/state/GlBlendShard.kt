package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendEquation
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.util.NeoColor

sealed interface GlBlendShard : GlDrawStateShard {
    interface Enabled : GlBlendShard {
        val color: NeoColor?
        val equation: GlBlendEquation
        val function: BlendFunction

        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.INSTANCE.blendEnabled.push(true)

            val color = color?.let { NeoGlStateManager.INSTANCE.blendColor.push(it) }
            val equation = NeoGlStateManager.INSTANCE.blendEquation.push(equation)
            val function = NeoGlStateManager.INSTANCE.blendFunction.push(function)

            return BoundResource.all(flag, color, equation, function)
        }
    }

    object Disabled : GlBlendShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.INSTANCE.blendEnabled.push(false)
        }
    }
}