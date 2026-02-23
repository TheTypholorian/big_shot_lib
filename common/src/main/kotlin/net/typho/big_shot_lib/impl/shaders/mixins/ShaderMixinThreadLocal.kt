package net.typho.big_shot_lib.impl.shaders.mixins

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixinManager

object ShaderMixinThreadLocal : ThreadLocal<Pair<ShaderProgramKey, ShaderMixinManager.Instance>>() {
    @JvmStatic
    fun push(key: ShaderProgramKey) {
        if (get() != null) {
            BigShotApi.LOGGER.warn("Pushing ShaderMixinThreadLocal with $key while ${get().first} is already bound, this might result in issues with shader mixins.")
        }

        ShaderMixinManager.INSTANCE.create(key)?.let {
            set(key to it)
        }
    }

    @JvmStatic
    fun pop() {
        remove()
    }
}