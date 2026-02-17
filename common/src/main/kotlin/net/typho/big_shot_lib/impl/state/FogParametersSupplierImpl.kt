package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.state.FogParameters
import net.typho.big_shot_lib.api.client.rendering.state.FogShape
import net.typho.big_shot_lib.api.util.IColor

class FogParametersSupplierImpl : FogParameters.Supplier {
    override fun get(): FogParameters {
        val mojang = RenderSystem.getShaderFog()
        return FogParameters(
            mojang.start,
            mojang.end,
            FogShape.entries[mojang.shape.ordinal],
            IColor.RGBAF(mojang.red, mojang.green, mojang.blue, mojang.alpha)
        )
    }
}