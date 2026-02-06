package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.api.gl_state.*
import net.typho.big_shot_lib.api.util.IColor
import java.util.*

interface NeoGlStateManager {
    fun enable(flag: GlFlag)

    fun disable(flag: GlFlag)

    fun blendColor(color: IColor)

    fun blendEquation(eq: BlendEquation)

    fun blendFunc(src: BlendFactor, dst: BlendFactor)

    fun blendFuncSeparate(src: BlendFactor, dst: BlendFactor, srcA: BlendFactor, dstA: BlendFactor)

    fun colorMask(mask: ColorMask)

    fun cullFace(face: CullFace)

    fun depthMask(mask: Boolean)

    fun depthFunc(func: DepthFunction)

    companion object : NeoGlStateManager {
        private val INSTANCE = ServiceLoader.load(NeoGlStateManager::class.java).findFirst().orElseThrow()

        override fun enable(flag: GlFlag) = INSTANCE.enable(flag)

        override fun disable(flag: GlFlag) = INSTANCE.disable(flag)

        override fun blendColor(color: IColor) = INSTANCE.blendColor(color)

        override fun blendEquation(eq: BlendEquation) = INSTANCE.blendEquation(eq)

        override fun blendFunc(src: BlendFactor, dst: BlendFactor) = INSTANCE.blendFunc(src, dst)

        override fun blendFuncSeparate(src: BlendFactor, dst: BlendFactor, srcA: BlendFactor, dstA: BlendFactor) = INSTANCE.blendFuncSeparate(src, dst, srcA, dstA)

        override fun colorMask(mask: ColorMask) = INSTANCE.colorMask(mask)

        override fun cullFace(face: CullFace) = INSTANCE.cullFace(face)

        override fun depthMask(mask: Boolean) = INSTANCE.depthMask(mask)

        override fun depthFunc(func: DepthFunction) = INSTANCE.depthFunc(func)
    }
}