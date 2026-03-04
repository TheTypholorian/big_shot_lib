package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.buffers.GlTexture2D
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.awt.Dimension

interface TextureUtil {
    val blockAtlasId: ResourceIdentifier
    val blockAtlas: GlTexture2D
        get() = getMinecraftTexture(blockAtlasId)

    //fun getRenderTypeTexture(type: RenderType): ResourceIdentifier?

    fun getMinecraftTexture(texture: ResourceIdentifier): GlTexture2D

    fun getTextureAtlasDimensions(atlas: ResourceIdentifier): Dimension

    companion object {
        @JvmField
        val INSTANCE: TextureUtil = TextureUtil::class.loadService()
    }
}