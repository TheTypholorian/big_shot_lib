package net.typho.big_shot_lib.api.client.rendering.quad

import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface NeoAtlas : NamedResource, GlTexture2D {
    val sprites: Map<NeoIdentifier, NeoAtlasSprite>
    override val width: Int
    override val height: Int

    companion object {
        val blocks: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "blocks"))!!
        val signs: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "signs"))!!
        val bannerPatterns: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "banner_patterns"))!!
        val shieldPatterns: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "shield_patterns"))!!
        val chest: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "chest"))!!
        val decoratedPot: NeoAtlas?
            get() = get(NeoIdentifier("minecraft", "decorated_pot"))
        val shulkerBoxes: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "shulker_boxes"))!!
        val beds: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "beds"))!!
        val particles: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "particles"))!!
        val paintings: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "paintings"))!!
        val mobEffects: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "mob_effects"))!!
        val mapDecorations: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "map_decorations"))!!
        val gui: NeoAtlas
            get() = get(NeoIdentifier("minecraft", "gui"))!!

        @JvmStatic
        operator fun get(location: NeoIdentifier) = InternalUtil.INSTANCE.getAtlas(location)
    }
}