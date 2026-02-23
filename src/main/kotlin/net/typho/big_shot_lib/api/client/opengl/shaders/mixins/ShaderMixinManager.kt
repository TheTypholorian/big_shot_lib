package net.typho.big_shot_lib.api.client.opengl.shaders.mixins

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceType
import java.nio.ByteOrder

interface ShaderMixinManager {
    val byteOrder: ByteOrder

    fun create(key: ShaderProgramKey): Instance?

    interface Instance {
        fun apply(type: ShaderSourceType, code: String): String

        fun <M> getOrCreateMixinInstance(mixin: ShaderMixin.Factory<M>): M?
    }

    companion object {
        @JvmField
        val INSTANCE: ShaderMixinManager = ShaderMixinManager::class.loadService()
    }
}