package net.typho.big_shot_lib

import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixinManager
import net.typho.big_shot_lib.impl.shaders.mixins.BreezeWindShaderMixin

object BigShotLib {
    fun init() {
        ShaderMixinManager.register(BreezeWindShaderMixin)
    }
}