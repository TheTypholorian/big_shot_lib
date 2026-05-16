package net.typho.big_shot_lib.api.client.rendering.opengl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.util.PolygonOffset
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.math.vec.IVec3

sealed interface GlLayeringShard : GlDrawStateShard {
    data class EnabledPolygonOffset(
        @JvmField
        val offset: PolygonOffset
    ) : GlLayeringShard {
        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.MAIN.polygonOffsetEnabled.push(true)
            val offset = NeoGlStateManager.MAIN.polygonOffset.push(offset)

            return BoundResource.all(flag, offset)
        }
    }

    data class EnabledViewOffset(
        @JvmField
        val scale: IVec3<Float>
    ) : GlLayeringShard {
        override fun bind(): BoundResource {
            val stack = RenderSystem.getModelViewStack()
            stack.pushMatrix()
            stack.scale(scale.x, scale.y, scale.z)
            RenderSystem.applyModelViewMatrix()

            return BoundResource {
                val stack = RenderSystem.getModelViewStack()
                stack.popMatrix()
                RenderSystem.applyModelViewMatrix()
            }
        }
    }

    object Disabled : GlLayeringShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.MAIN.polygonOffsetEnabled.push(false)
        }
    }
}