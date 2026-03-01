package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixin

interface ShaderMixinFactory {
    fun register(mixin: ShaderMixin, builtin: Boolean = false)

    fun register(mixin: ShaderMixin.Factory<*>, builtin: Boolean = false)
}