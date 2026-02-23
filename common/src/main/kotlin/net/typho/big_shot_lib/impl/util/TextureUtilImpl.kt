package net.typho.big_shot_lib.impl.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.client.renderer.texture.TextureAtlas
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.opengl.buffers.GlTexture2D
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoTexture2D
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.client.opengl.util.TextureType
import net.typho.big_shot_lib.api.client.opengl.util.TextureUtil
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.lwjgl.opengl.GL11.*

class TextureUtilImpl : TextureUtil {
    private val textures = HashMap<AbstractTexture, GlTexture2D>()
    @Suppress("DEPRECATION")
    override val blockAtlasTexture = TextureAtlas.LOCATION_BLOCKS.toNeo()

    override fun getMinecraftTexture(texture: ResourceIdentifier): GlTexture2D {
        return textures.computeIfAbsent(Minecraft.getInstance().textureManager.getTexture(texture.toMojang())) { texture ->
            texture.bind()
            val format = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT)
            GlStateStack.textures[TextureType.TEXTURE_2D]?.rebind()
            return@computeIfAbsent NeoTexture2D(texture.id, TextureFormat.entries.first { it.internalId == format }, false)
        }
    }
}