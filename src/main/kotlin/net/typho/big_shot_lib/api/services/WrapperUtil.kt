package net.typho.big_shot_lib.api.services

import com.mojang.blaze3d.pipeline.RenderTarget
import net.minecraft.core.Registry
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebuffer
import net.typho.big_shot_lib.api.util.NeoRegistry
import java.util.*

interface WrapperUtil {
    fun wrap(manager: ResourceManager): ResourceManagerWrapper

    fun wrap(target: RenderTarget): GlFramebuffer

    fun <T> wrap(registry: Registry<T>): NeoRegistry<T>

    companion object {
        @JvmField
        val INSTANCE: WrapperUtil = ServiceLoader.load(WrapperUtil::class.java).findFirst().orElseThrow()
    }
}