package net.typho.big_shot_lib.impl.util

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.util.quads.NeoAtlas
import net.typho.big_shot_lib.api.client.util.quads.NeoAtlasSprite
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

data class NeoAtlasSpriteImpl(
    override val atlas: NeoAtlas,
    @JvmField
    val sprite: TextureAtlasSprite
) : NeoAtlasSprite {
    override val x: Int
        get() = sprite.x
    override val y: Int
        get() = sprite.y
    override val width: Int
        get() = sprite.contents().width()
    override val height: Int
        get() = sprite.contents().height()
    override val u0: Float
        get() = sprite.u0
    override val u1: Float
        get() = sprite.u1
    override val v0: Float
        get() = sprite.v0
    override val v1: Float
        get() = sprite.v1
    override val location: ResourceIdentifier
        get() = sprite.contents().name().toNeo()
}