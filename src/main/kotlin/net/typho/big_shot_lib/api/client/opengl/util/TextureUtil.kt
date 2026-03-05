package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.buffers.GlTexture2D
import net.typho.big_shot_lib.api.client.util.quads.NeoAtlas
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface TextureUtil {
    val blockAtlas: NeoAtlas
    val signsAtlas: NeoAtlas
    val bannerPatternsAtlas: NeoAtlas
    val shieldPatternsAtlas: NeoAtlas
    val chestAtlas: NeoAtlas
    val decoratedPotAtlas: NeoAtlas?
    val shulkerBoxesAtlas: NeoAtlas
    val bedsAtlas: NeoAtlas
    val particlesAtlas: NeoAtlas
    val paintingsAtlas: NeoAtlas
    val mobEffectsAtlas: NeoAtlas
    val mapDecorationsAtlas: NeoAtlas
    val guiAtlas: NeoAtlas

    fun getMinecraftTexture(texture: ResourceIdentifier): GlTexture2D

    fun getAtlas(texture: ResourceIdentifier): NeoAtlas

    companion object {
        @JvmField
        val INSTANCE: TextureUtil = TextureUtil::class.loadService()
    }
}