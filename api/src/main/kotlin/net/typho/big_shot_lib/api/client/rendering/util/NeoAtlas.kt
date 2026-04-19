package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

abstract class NeoAtlas : NamedResource, GlTexture2D {
    abstract val sprites: Map<NeoIdentifier, NeoAtlasSprite>
    abstract override val width: Int
    abstract override val height: Int

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NeoAtlas) return false

        if (glId != other.glId) return false

        return true
    }

    override fun hashCode(): Int {
        return glId
    }

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