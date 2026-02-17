package net.typho.big_shot_lib.api.services

import com.mojang.blaze3d.pipeline.RenderTarget
import net.minecraft.core.Registry
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebuffer
import net.typho.big_shot_lib.api.util.NeoRegistry

interface WrapperUtil {
    fun wrap(manager: ResourceManager): ResourceManagerWrapper

    fun wrap(target: RenderTarget): GlFramebuffer

    fun <T> wrap(registry: Registry<T>): NeoRegistry<T>

    companion object {
        @JvmField
        val INSTANCE: WrapperUtil = WrapperUtil::class.loadService()
    }
}