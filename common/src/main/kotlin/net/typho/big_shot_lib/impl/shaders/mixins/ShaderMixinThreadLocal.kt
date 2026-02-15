package net.typho.big_shot_lib.impl.shaders.mixins

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixinManager

object ShaderMixinThreadLocal : ThreadLocal<ShaderMixinManager.Instance>() {
    @JvmStatic
    fun push(key: ShaderProgramKey) {
        if (get() != null) {
            BigShotApi.LOGGER.warn("Pushing ShaderMixinThreadLocal with $key while ${get()} is already bound, this might result in issues with shader mixins.")
        }

        set(ShaderMixinManager.create(key))
    }

    @JvmStatic
    fun pop() {
        remove()
    }
}