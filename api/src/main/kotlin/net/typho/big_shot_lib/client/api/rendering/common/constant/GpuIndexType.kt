package net.typho.big_shot_lib.client.api.rendering.common.constant

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import org.jetbrains.annotations.ApiStatus

private val INSTANCE by lazy { GpuIndexType.Values::class.loadService() }

@ApiStatus.NonExtendable
interface GpuIndexType : Extension<Any> {
    companion object : Values by INSTANCE

    interface Values {
        val codec: Codec<GpuIndexType>

        val short: GpuIndexType
        val int: GpuIndexType
    }
}