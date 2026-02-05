package net.typho.big_shot_lib.impl.shaders.mixins

import net.typho.big_shot_lib.api.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixinManager
import java.util.*

class ShaderMixinManagerImpl : ShaderMixinManager {
    @JvmField
    val mixins = LinkedList<ShaderMixin.Factory>()

    override fun register(mixin: ShaderMixin.Factory) {
        mixins.add(mixin)
    }

    override fun apply(key: ShaderSourceKey, code: String): String {
        val applied = mixins.map { factory -> factory.create(key) }
    }
}