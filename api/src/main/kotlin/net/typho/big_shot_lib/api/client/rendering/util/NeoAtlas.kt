package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.NamedResource

abstract class NeoAtlas : NamedResource, GlTexture2D {
    abstract val sprites: Map<Identifier, NeoAtlasSprite>
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
            get() = get(Identifier.minecraft("blocks"))!!
        val signs: NeoAtlas
            get() = get(Identifier.minecraft("signs"))!!
        val bannerPatterns: NeoAtlas
            get() = get(Identifier.minecraft("banner_patterns"))!!
        val shieldPatterns: NeoAtlas
            get() = get(Identifier.minecraft("shield_patterns"))!!
        val chest: NeoAtlas
            get() = get(Identifier.minecraft("chest"))!!
        val decoratedPot: NeoAtlas?
            get() = get(Identifier.minecraft("decorated_pot"))
        val shulkerBoxes: NeoAtlas
            get() = get(Identifier.minecraft("shulker_boxes"))!!
        val beds: NeoAtlas
            get() = get(Identifier.minecraft("beds"))!!
        val particles: NeoAtlas
            get() = get(Identifier.minecraft("particles"))!!
        val paintings: NeoAtlas
            get() = get(Identifier.minecraft("paintings"))!!
        val mobEffects: NeoAtlas
            get() = get(Identifier.minecraft("mob_effects"))!!
        val mapDecorations: NeoAtlas
            get() = get(Identifier.minecraft("map_decorations"))!!
        val gui: NeoAtlas
            get() = get(Identifier.minecraft("gui"))!!

        @JvmStatic
        operator fun get(location: Identifier) = InternalUtil.INSTANCE.getAtlas(location)
    }
}