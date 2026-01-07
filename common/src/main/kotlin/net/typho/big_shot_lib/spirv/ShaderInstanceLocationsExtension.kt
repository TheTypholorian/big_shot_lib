package net.typho.big_shot_lib.spirv

import net.minecraft.client.renderer.EffectInstance
import net.minecraft.client.renderer.ShaderInstance

interface ShaderInstanceLocationsExtension {
    fun getLocations(): ShaderLocationsInfo?

    fun setLocations(locations: ShaderLocationsInfo?)

    companion object {
        fun EffectInstance.getLocations() = (this as ShaderInstanceLocationsExtension).getLocations()

        fun EffectInstance.setLocations(locations: ShaderLocationsInfo?) = (this as ShaderInstanceLocationsExtension).setLocations(locations)

        fun ShaderInstance.getLocations() = (this as ShaderInstanceLocationsExtension).getLocations()

        fun ShaderInstance.setLocations(locations: ShaderLocationsInfo?) = (this as ShaderInstanceLocationsExtension).setLocations(locations)
    }
}