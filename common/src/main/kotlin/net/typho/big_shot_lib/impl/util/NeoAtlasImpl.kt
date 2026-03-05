package net.typho.big_shot_lib.impl.util

import net.minecraft.client.renderer.texture.TextureAtlas
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoTexture2D
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.client.util.quads.NeoAtlas
import net.typho.big_shot_lib.api.client.util.quads.NeoAtlasSprite
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.mixin.util.TextureAtlasAccessor
import java.util.function.Consumer

data class NeoAtlasImpl(
    @JvmField
    val atlas: TextureAtlas
) : NeoTexture2D(
    atlas.id,
    TextureFormat.RGBA,
    false
), NeoAtlas {
    override val sprites: Map<ResourceIdentifier, NeoAtlasSprite>
        get() = (atlas as TextureAtlasAccessor).texturesByName
            .mapKeys { it.key.toNeo() }
            .mapValues { NeoAtlasSpriteImpl(this, it.value) }
    override val width: Int
        get() = (atlas as TextureAtlasAccessor).width
    override val height: Int
        get() = (atlas as TextureAtlasAccessor).height
    override val mipLevel: Int
        get() = (atlas as TextureAtlasAccessor).mipLevel
    override val location: ResourceIdentifier
        get() = atlas.location().toNeo()

    override fun resize(width: Int, height: Int, upload: Consumer<BufferUploader>) {
        throw UnsupportedOperationException("Cannot resize vanilla texture atlas ${atlas.location()}")
    }
}