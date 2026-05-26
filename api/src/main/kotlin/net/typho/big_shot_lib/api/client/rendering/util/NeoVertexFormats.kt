package net.typho.big_shot_lib.api.client.rendering.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.serialization.Codec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.BigShotApi.lookupOrThrow
import net.typho.big_shot_lib.api.InternalUtil

object NeoVertexFormats {
    @JvmField
    val REGISTRY: BiMap<Identifier, NeoVertexFormat> = HashBiMap.create()
    @JvmField
    val CODEC: Codec<NeoVertexFormat> = Identifier.CODEC.xmap(
        { REGISTRY[it] },
        { REGISTRY.inverse()[it] }
    )

    fun register(location: Identifier, format: NeoVertexFormat) {
        REGISTRY[location] = format
    }

    init {
        register(Identifier.minecraft("blit_screen"), InternalUtil.INSTANCE.blitScreenVertexFormat)
        register(Identifier.minecraft("block"), InternalUtil.INSTANCE.blockVertexFormat)
        register(Identifier.minecraft("new_entity"), InternalUtil.INSTANCE.newEntityVertexFormat)
        register(Identifier.minecraft("particle"), InternalUtil.INSTANCE.particleVertexFormat)
        register(Identifier.minecraft("position"), InternalUtil.INSTANCE.positionVertexFormat)
        register(Identifier.minecraft("position_color"), InternalUtil.INSTANCE.positionColorVertexFormat)
        register(Identifier.minecraft("position_color_normal"), InternalUtil.INSTANCE.positionColorNormalVertexFormat)
        register(Identifier.minecraft("position_color_lightmap"), InternalUtil.INSTANCE.positionColorLightVertexFormat)
        register(Identifier.minecraft("position_tex"), InternalUtil.INSTANCE.positionTexVertexFormat)
        register(Identifier.minecraft("position_tex_color"), InternalUtil.INSTANCE.positionTexColorVertexFormat)
        register(Identifier.minecraft("position_color_tex_lightmap"), InternalUtil.INSTANCE.positionColorTexLightVertexFormat)
        register(Identifier.minecraft("position_tex_lightmap_color"), InternalUtil.INSTANCE.positionTexLightColorVertexFormat)
        register(Identifier.minecraft("position_tex_color_normal"), InternalUtil.INSTANCE.positionTexColorNormalVertexFormat)
    }

    /**
     * - Position
     */
    @JvmField
    val BLIT_SCREEN = InternalUtil.INSTANCE.blitScreenVertexFormat
    /**
     * - Position
     * - Color
     * - Texture UV
     * - Light UV
     * - Normal
     * - 1 byte padding
     */
    @JvmField
    val BLOCK = InternalUtil.INSTANCE.blockVertexFormat
    /**
     * - Position
     * - Color
     * - Texture UV
     * - Overlay UV
     * - Light UV
     * - Normal
     * - 1 byte padding
     */
    @JvmField
    val NEW_ENTITY = InternalUtil.INSTANCE.newEntityVertexFormat
    /**
     * - Position
     * - Texture UV
     * - Color
     * - Light UV
     */
    @JvmField
    val PARTICLE = InternalUtil.INSTANCE.particleVertexFormat
    /**
     * - Position
     */
    @JvmField
    val POSITION = InternalUtil.INSTANCE.positionVertexFormat
    /**
     * - Position
     * - Color
     */
    @JvmField
    val POSITION_COLOR = InternalUtil.INSTANCE.positionColorVertexFormat
    /**
     * - Position
     * - Color
     * - Normal
     * - 1 byte padding
     */
    @JvmField
    val POSITION_COLOR_NORMAL = InternalUtil.INSTANCE.positionColorNormalVertexFormat
    /**
     * - Position
     * - Color
     * - Light UV
     */
    @JvmField
    val POSITION_COLOR_LIGHTMAP = InternalUtil.INSTANCE.positionColorLightVertexFormat
    /**
     * - Position
     * - Texture UV
     */
    @JvmField
    val POSITION_TEX = InternalUtil.INSTANCE.positionTexVertexFormat
    /**
     * - Position
     * - Texture UV
     * - Color
     */
    @JvmField
    val POSITION_TEX_COLOR = InternalUtil.INSTANCE.positionTexColorVertexFormat
    /**
     * - Position
     * - Color
     * - Texture UV
     * - Light UV
     */
    @JvmField
    val POSITION_COLOR_TEX_LIGHTMAP = InternalUtil.INSTANCE.positionColorTexLightVertexFormat
    /**
     * - Position
     * - Texture UV
     * - Light UV
     * - Color
     */
    @JvmField
    val POSITION_TEX_LIGHTMAP_COLOR = InternalUtil.INSTANCE.positionTexLightColorVertexFormat
    /**
     * - Position
     * - Texture UV
     * - Color
     * - Normal
     * - 1 byte padding
     */
    @JvmField
    val POSITION_TEX_COLOR_NORMAL = InternalUtil.INSTANCE.positionTexColorNormalVertexFormat
}