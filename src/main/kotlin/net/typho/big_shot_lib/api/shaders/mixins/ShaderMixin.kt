package net.typho.big_shot_lib.api.shaders.mixins

import net.typho.big_shot_lib.api.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.shaders.ShaderSourceKey

interface ShaderMixin {
    fun mixinPreCompile(key: ShaderSourceKey, code: String): String = code

    fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer = code

    fun mixinPostCompile(key: ShaderSourceKey, code: String): String = code

    fun then(other: ShaderMixin) = object : ShaderMixin {
        override fun mixinPreCompile(key: ShaderSourceKey, code: String): String {
            return other.mixinPreCompile(key, this@ShaderMixin.mixinPreCompile(key, code))
        }

        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            return other.mixinBytecode(key, this@ShaderMixin.mixinBytecode(key, code))
        }

        override fun mixinPostCompile(key: ShaderSourceKey, code: String): String {
            return other.mixinPostCompile(key, this@ShaderMixin.mixinPostCompile(key, code))
        }
    }

    fun interface Factory {
        fun create(key: ShaderProgramKey, parent: ShaderMixinManager.Instance): ShaderMixin?
    }
}