package net.typho.big_shot_lib.client.api.rendering.common.constant

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import org.jetbrains.annotations.ApiStatus

private val INSTANCE by lazy { GpuBlendFactor.Values::class.loadService() }

@ApiStatus.NonExtendable
interface GpuBlendFactor : Extension<Any> {
    companion object : Values by INSTANCE

    interface Values {
        val codec: Codec<GpuBlendFactor>

        val zero: GpuBlendFactor
        val one: GpuBlendFactor

        val srcColor: GpuBlendFactor
        val oneMinusSrcColor: GpuBlendFactor
        val dstColor: GpuBlendFactor
        val oneMinusDstColor: GpuBlendFactor

        val srcAlpha: GpuBlendFactor
        val oneMinusSrcAlpha: GpuBlendFactor
        val dstAlpha: GpuBlendFactor
        val oneMinusDstAlpha: GpuBlendFactor

        val constantColor: GpuBlendFactor
        val oneMinusConstantColor: GpuBlendFactor
        val constantAlpha: GpuBlendFactor
        val oneMinusConstantAlpha: GpuBlendFactor

        val srcAlphaSaturate: GpuBlendFactor
    }
}