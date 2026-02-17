package net.typho.big_shot_lib.api.services

import com.mojang.blaze3d.pipeline.RenderTarget
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.meshes.TexturedQuad
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebuffer
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.NeoRegistryAccess

interface WrapperUtil {
    fun wrap(manager: ResourceManager): NeoResourceManager

    fun wrap(target: RenderTarget): GlFramebuffer

    fun <T> wrap(registry: Registry<T>): NeoRegistry<T>

    fun wrap(access: RegistryAccess): NeoRegistryAccess

    fun wrap(quad: BakedQuad): TexturedQuad

    companion object {
        @JvmField
        val INSTANCE: WrapperUtil = WrapperUtil::class.loadService()
    }
}