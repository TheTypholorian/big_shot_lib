package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.state.FogParameters
import net.typho.big_shot_lib.api.client.rendering.state.FogShape
import net.typho.big_shot_lib.api.util.IColor

class FogParametersSupplierImpl : FogParameters.Supplier {
    override fun get(): FogParameters {
        return FogParameters(
            RenderSystem.getShaderFogStart(),
            RenderSystem.getShaderFogEnd(),
            FogShape.entries[RenderSystem.getShaderFogShape().ordinal],
            IColor.RGBAF(RenderSystem.getShaderFogColor())
        )
    }
}