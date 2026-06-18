package net.typho.big_shot_lib.api.util

import net.minecraft.client.renderer.MultiBufferSource
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.client.rendering.util.MultiBufferSourceInjection

interface WrapperUtil {
    fun inject(vanilla: MultiBufferSource.BufferSource, injection: MultiBufferSourceInjection): MultiBufferSource.BufferSource

    fun uninject(vanilla: MultiBufferSource.BufferSource, injection: MultiBufferSourceInjection): MultiBufferSource.BufferSource

    companion object {
        @JvmStatic
        @get:JvmName("getInstance")
        val INSTANCE by lazy { WrapperUtil::class.loadService() }
    }
}