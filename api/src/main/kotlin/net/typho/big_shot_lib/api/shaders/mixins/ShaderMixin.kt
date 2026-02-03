package net.typho.big_shot_lib.api.shaders.mixins

import net.typho.big_shot_lib.api.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.shaders.ShaderSourceKey

interface ShaderMixin {
    fun mixinPreCompile(key: ShaderSourceKey, code: String): String = code

    fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer = code

    fun mixinPostCompile(key: ShaderSourceKey, code: String): String = code

    fun interface Factory {
        fun create(key: ShaderProgramKey): ShaderMixin
    }
}