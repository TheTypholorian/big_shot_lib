package net.typho.big_shot_lib.api.client.rendering.quad

import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface NeoAtlas : NamedResource, GlTexture2D {
    val sprites: Map<NeoIdentifier, NeoAtlasSprite>
    override val width: Int
    override val height: Int
    val mipLevel: Int

    companion object {
        @JvmField
        val blocks = get(NeoIdentifier("minecraft", "blocks"))!!
        @JvmField
        val signs = get(NeoIdentifier("minecraft", "signs"))!!
        @JvmField
        val bannerPatterns = get(NeoIdentifier("minecraft", "banner_patterns"))!!
        @JvmField
        val shieldPatterns = get(NeoIdentifier("minecraft", "shield_patterns"))!!
        @JvmField
        val chest = get(NeoIdentifier("minecraft", "chest"))!!
        @JvmField
        val decoratedPot = get(NeoIdentifier("minecraft", "decorated_pot"))
        @JvmField
        val shulkerBoxes = get(NeoIdentifier("minecraft", "shulker_boxes"))!!
        @JvmField
        val beds = get(NeoIdentifier("minecraft", "beds"))!!
        @JvmField
        val particles = get(NeoIdentifier("minecraft", "particles"))!!
        @JvmField
        val paintings = get(NeoIdentifier("minecraft", "paintings"))!!
        @JvmField
        val mobEffects = get(NeoIdentifier("minecraft", "mob_effects"))!!
        @JvmField
        val mapDecorations = get(NeoIdentifier("minecraft", "map_decorations"))!!
        @JvmField
        val gui = get(NeoIdentifier("minecraft", "gui"))!!

        @JvmStatic
        operator fun get(location: NeoIdentifier) = InternalUtil.INSTANCE.getAtlas(location)
    }
}