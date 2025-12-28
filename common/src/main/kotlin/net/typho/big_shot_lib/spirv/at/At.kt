package net.typho.big_shot_lib.spirv.at

import net.typho.big_shot_lib.spirv.ShaderMixinContext

interface At {
    fun getStart(context: ShaderMixinContext): Int

    fun getEnd(context: ShaderMixinContext): Int? = null
}