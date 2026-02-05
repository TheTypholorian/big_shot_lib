package net.typho.big_shot_lib.impl

import com.mojang.blaze3d.platform.GlStateManager
import net.typho.big_shot_lib.api.GlManager
import net.typho.big_shot_lib.api.render_queue.shards.*
import net.typho.big_shot_lib.api.util.IColor
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14.glBlendColor

class GlManagerImpl : GlManager {
    override fun enable(flag: GlFlag) {
        glEnable(flag.id)
    }

    override fun disable(flag: GlFlag) {
        glDisable(flag.id)
    }

    override fun blendColor(color: IColor) {
        glBlendColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)
    }

    override fun blendEquation(eq: BlendEquation) {
        GlStateManager._blendEquation(eq.glId)
    }

    override fun blendFunc(
        src: BlendFactor,
        dst: BlendFactor
    ) {
        GlStateManager._blendFunc(src.glId, dst.glId)
    }

    override fun blendFuncSeparate(
        src: BlendFactor,
        dst: BlendFactor,
        srcA: BlendFactor,
        dstA: BlendFactor
    ) {
        GlStateManager._blendFuncSeparate(src.glId, dst.glId, srcA.glId, dstA.glId)
    }

    override fun colorMask(mask: ColorMask) {
        GlStateManager._colorMask(mask.red, mask.green, mask.blue, mask.alpha)
    }

    override fun cullFace(face: CullFace) {
        glCullFace(face.glId)
    }

    override fun depthMask(mask: Boolean) {
        GlStateManager._depthMask(mask)
    }

    override fun depthFunc(func: DepthFunction) {
        GlStateManager._depthFunc(func.glId)
    }
}