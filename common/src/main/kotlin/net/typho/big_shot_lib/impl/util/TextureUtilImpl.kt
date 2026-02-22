package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.opengl.GlConst
import com.mojang.blaze3d.opengl.GlTexture
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.client.renderer.texture.TextureAtlas
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.rendering.services.TextureUtil
import net.typho.big_shot_lib.api.client.rendering.textures.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.textures.NeoTexture2D
import net.typho.big_shot_lib.api.client.rendering.textures.TextureFormat
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import kotlin.jvm.optionals.getOrNull

class TextureUtilImpl : TextureUtil {
    private val textures = HashMap<AbstractTexture, GlTexture2D>()

    @Suppress("DEPRECATION")
    override fun blockAtlasTexture(): ResourceIdentifier {
        return TextureAtlas.LOCATION_BLOCKS.toNeo()
    }

    override fun getRenderTypeTexture(type: RenderType): ResourceIdentifier? {
        return when (type) {
            is RenderType.CompositeRenderType -> {
                type.state.textureState.cutoutTexture().getOrNull()?.toNeo()
            }
            else -> null
        }
    }

    override fun getMinecraftTexture(texture: ResourceIdentifier): GlTexture2D {
        return textures.computeIfAbsent(Minecraft.getInstance().textureManager.getTexture(texture.toMojang())) { texture ->
            val texture = texture.texture as GlTexture
            val formatId = GlConst.toGlExternalId(texture.format)
            return@computeIfAbsent NeoTexture2D(texture.glId(), TextureFormat.entries.first { it.internalId == formatId }, false)
        }
    }
}