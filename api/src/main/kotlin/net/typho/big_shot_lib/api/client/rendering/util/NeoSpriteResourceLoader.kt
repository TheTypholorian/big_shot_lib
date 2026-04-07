package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.client.renderer.texture.SpriteLoader
import net.minecraft.server.packs.metadata.MetadataSectionSerializer
import net.minecraft.server.packs.resources.Resource
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface NeoSpriteResourceLoader {
    fun loadSprite(location: NeoIdentifier, resource: Resource): NeoSpriteContents?

    companion object {
        @JvmStatic
        @JvmOverloads
        fun create(sections: Collection<MetadataSectionSerializer<*>> = SpriteLoader.DEFAULT_METADATA_SECTIONS): NeoSpriteResourceLoader = InternalUtil.INSTANCE.createSpriteResourceLoader(sections)
    }
}