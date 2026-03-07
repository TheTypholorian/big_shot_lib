package net.typho.big_shot_lib.impl.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.texture.TextureAtlas
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.opengl.buffers.GlTexture2D
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoTexture2D
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.client.opengl.util.TextureType
import net.typho.big_shot_lib.api.client.opengl.util.TextureUtil
import net.typho.big_shot_lib.api.client.util.quads.NeoAtlas
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.lwjgl.opengl.GL11.*

class TextureUtilImpl : TextureUtil {
    @Suppress("DEPRECATION")
    override val blockAtlas: NeoAtlas
        get() = getAtlas(TextureAtlas.LOCATION_BLOCKS.toNeo())
    override val signsAtlas: NeoAtlas
        get() = getAtlas(Sheets.SIGN_SHEET.toNeo())
    override val bannerPatternsAtlas: NeoAtlas
        get() = getAtlas(Sheets.BANNER_SHEET.toNeo())
    override val shieldPatternsAtlas: NeoAtlas
        get() = getAtlas(Sheets.SHIELD_SHEET.toNeo())
    override val chestAtlas: NeoAtlas
        get() = getAtlas(Sheets.CHEST_SHEET.toNeo())
    override val decoratedPotAtlas: NeoAtlas
        get() = getAtlas(Sheets.DECORATED_POT_SHEET.toNeo())
    override val shulkerBoxesAtlas: NeoAtlas
        get() = getAtlas(Sheets.SHULKER_SHEET.toNeo())
    override val bedsAtlas: NeoAtlas
        get() = getAtlas(Sheets.BED_SHEET.toNeo())
    @Suppress("DEPRECATION")
    override val particlesAtlas: NeoAtlas
        get() = getAtlas(TextureAtlas.LOCATION_PARTICLES.toNeo())
    override val paintingsAtlas: NeoAtlas
        get() = getAtlas(ResourceIdentifier("textures/atlas/paintings.png"))
    override val mobEffectsAtlas: NeoAtlas
        get() = getAtlas(ResourceIdentifier("textures/atlas/mob_effects.png"))
    override val mapDecorationsAtlas: NeoAtlas
        get() = getAtlas(ResourceIdentifier("textures/atlas/map_decorations.png"))
    override val guiAtlas: NeoAtlas
        get() = getAtlas(ResourceIdentifier("textures/atlas/gui.png"))

    override fun getMinecraftTexture(texture: ResourceIdentifier): GlTexture2D {
        val texture = Minecraft.getInstance().textureManager.getTexture(texture.toMojang())

        texture.bind()
        val format = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT)
        GlStateStack.textures[TextureType.TEXTURE_2D]?.rebind()
        return NeoTexture2D(texture.id, TextureFormat.entries.first { it.internalId == format }, false)
    }

    override fun getAtlas(texture: ResourceIdentifier): NeoAtlas {
        return NeoAtlasImpl(Minecraft.getInstance().modelManager.getAtlas(texture.toMojang()))
    }
}