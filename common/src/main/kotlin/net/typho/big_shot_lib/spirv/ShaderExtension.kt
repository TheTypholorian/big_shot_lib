package net.typho.big_shot_lib.spirv

import net.minecraft.client.renderer.EffectInstance
import net.minecraft.client.renderer.ShaderInstance
import net.typho.big_shot_lib.api.IShader

interface ShaderExtension {
    fun `big_shot_lib$getLocations`(): ShaderLocationsInfo?

    companion object {
        fun EffectInstance.getLocations() = (this as ShaderExtension).`big_shot_lib$getLocations`()

        fun ShaderInstance.getLocations() = (this as ShaderExtension).`big_shot_lib$getLocations`()

        fun IShader.getLocations() = `big_shot_lib$getLocations`()
    }
}