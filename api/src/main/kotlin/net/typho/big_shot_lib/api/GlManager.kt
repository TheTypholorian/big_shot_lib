package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.api.render_queue.shards.*
import net.typho.big_shot_lib.api.util.IColor
import java.util.*

interface GlManager {
    fun blendColor(color: IColor)

    fun blendEquation(eq: BlendEquation)

    fun blendFunc(src: BlendFactor, dst: BlendFactor)

    fun blendFuncSeparate(src: BlendFactor, dst: BlendFactor, srcA: BlendFactor, dstA: BlendFactor)

    fun colorMask(mask: ColorMask)

    fun cullFace(face: CullFace)

    fun enable(flag: GlFlag)

    fun disable(flag: GlFlag)

    companion object : GlManager {
        private val INSTANCE = ServiceLoader.load(GlManager::class.java).findFirst().orElseThrow()

        override fun blendColor(color: IColor) = INSTANCE.blendColor(color)

        override fun blendEquation(eq: BlendEquation) = INSTANCE.blendEquation(eq)

        override fun blendFunc(src: BlendFactor, dst: BlendFactor) = INSTANCE.blendFunc(src, dst)

        override fun blendFuncSeparate(src: BlendFactor, dst: BlendFactor, srcA: BlendFactor, dstA: BlendFactor) = INSTANCE.blendFuncSeparate(src, dst, srcA, dstA)

        override fun colorMask(mask: ColorMask) = INSTANCE.colorMask(mask)

        override fun cullFace(face: CullFace) = INSTANCE.cullFace(face)

        override fun enable(flag: GlFlag) = INSTANCE.enable(flag)

        override fun disable(flag: GlFlag) = INSTANCE.disable(flag)
    }
}