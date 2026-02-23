package net.typho.big_shot_lib.client

import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.ShaderMixinFactory
import net.typho.big_shot_lib.impl.shaders.mixins.BreezeWindShaderMixin

object BigShotClient : BigShotClientEntrypoint {
    override fun registerShaderMixins(factory: ShaderMixinFactory) {
        factory.register(BreezeWindShaderMixin)
    }
}