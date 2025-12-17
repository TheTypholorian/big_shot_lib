package net.typho.big_shot_lib.spirv

import net.minecraft.resources.ResourceLocation
import java.util.*

interface ShaderMixinCallback {
    fun modify(shader: ResourceLocation)

    companion object {
        val callbacks = LinkedList<ShaderMixinCallback>()

        fun register(callback: ShaderMixinCallback) {
        }
    }
}