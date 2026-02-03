package net.typho.big_shot_lib.api.shaders.mixins

import net.typho.big_shot_lib.api.Services

interface ShaderMixinManager {
    companion object {
        private val INSTANCE = Services.load(ShaderMixinManager.Companion::class.java)
    }
}