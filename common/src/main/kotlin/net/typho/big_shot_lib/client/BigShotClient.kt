package net.typho.big_shot_lib.client

import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.ShaderMixinFactory

object BigShotClient : BigShotClientEntrypoint {
    override fun registerShaderMixins(factory: ShaderMixinFactory) {
        factory.register(object : ShaderMixin {
            override fun mixinPostCompile(key: ShaderSourceKey, code: String): String {
                println(key)
                println(code)
                return super.mixinPostCompile(key, code)
            }
        })
    }
}