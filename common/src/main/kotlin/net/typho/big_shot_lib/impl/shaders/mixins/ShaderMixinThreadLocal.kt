package net.typho.big_shot_lib.impl.shaders.mixins

import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixinManager

object ShaderMixinThreadLocal : ThreadLocal<ShaderMixinManager.Instance>() {
    @JvmStatic
    fun push(key: ShaderProgramKey) {
        if (get() != null) {
            throw IllegalStateException()
        }

        set(ShaderMixinManager.create(key))
    }

    @JvmStatic
    fun pop() {
        remove()
    }
}