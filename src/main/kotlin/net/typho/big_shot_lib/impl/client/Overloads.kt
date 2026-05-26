package net.typho.big_shot_lib.impl.client

import net.minecraft.client.renderer.RenderType
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.util.getExtensionValue

object Overloads {
    @JvmStatic
    fun convertRenderType(neo: NeoRenderType) = neo.getExtensionValue<RenderType>()
}