package net.typho.big_shot_lib

import net.typho.big_shot_lib.api.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixinManager
import net.typho.big_shot_lib.impl.shaders.mixins.BreezeWindShaderMixin

object BigShotLib {
    fun init() {
        ShaderMixinManager.register(BreezeWindShaderMixin)
        ShaderMixinManager.register { key ->
            println("Creating mixin for $key")

            object : ShaderMixin {
                override fun mixinPreCompile(key: ShaderSourceKey, code: String): String {
                    println("\tPre compile size for ${key.type} is ${code.length} characters")
                    return super.mixinPreCompile(key, code)
                }

                override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
                    println("\tBytecode size for ${key.type} is ${code.buffer.capacity()} words")
                    return super.mixinBytecode(key, code)
                }
            }
        }
    }
}