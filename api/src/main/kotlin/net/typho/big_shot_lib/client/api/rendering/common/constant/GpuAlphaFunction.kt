package net.typho.big_shot_lib.client.api.rendering.common.constant

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import org.jetbrains.annotations.ApiStatus

private val INSTANCE by lazy { GpuAlphaFunction.Values::class.loadService() }

@ApiStatus.NonExtendable
interface GpuAlphaFunction : Extension<Any> {
    companion object : Values by INSTANCE

    interface Values {
        val codec: Codec<GpuAlphaFunction>

        val never: GpuAlphaFunction
        val less: GpuAlphaFunction
        val equal: GpuAlphaFunction
        val lequal: GpuAlphaFunction
        val greater: GpuAlphaFunction
        val notEqual: GpuAlphaFunction
        val gequal: GpuAlphaFunction
        val always: GpuAlphaFunction
    }
}