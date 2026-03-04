package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.opengl.GlConst
import com.mojang.blaze3d.opengl.GlTexture
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.client.renderer.texture.TextureAtlas
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.opengl.buffers.GlTexture2D
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoTexture2D
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.client.opengl.util.TextureUtil
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.mixin.util.TextureAtlasAccessor
import org.lwjgl.opengl.GL11.*
import java.awt.Dimension

class TextureUtilImpl : TextureUtil {
    private val textures = HashMap<AbstractTexture, GlTexture2D>()
    @Suppress("DEPRECATION")
    override val blockAtlasId = TextureAtlas.LOCATION_BLOCKS.toNeo()

    override fun getMinecraftTexture(texture: ResourceIdentifier): GlTexture2D {
        return textures.computeIfAbsent(Minecraft.getInstance().textureManager.getTexture(texture.toMojang())) { texture ->
            val texture = texture.texture as GlTexture
            val formatId = GlConst.toGlExternalId(texture.format)
            return@computeIfAbsent NeoTexture2D(texture.glId(), TextureFormat.entries.first { it.internalId == formatId }, false)
        }
    }

    override fun getTextureAtlasDimensions(atlas: ResourceIdentifier): Dimension {
        val atlas = Minecraft.getInstance().getTextureAtlas(atlas.toMojang()) as TextureAtlasAccessor
        return Dimension(atlas.width, atlas.height)
    }
}