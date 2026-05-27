package net.typho.big_shot_lib.api.client.rendering.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import com.mojang.serialization.Codec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.BigShotApi.lookupOrThrow
import net.typho.big_shot_lib.api.InternalUtil

object NeoVertexFormats {
    @JvmField
    val REGISTRY: BiMap<Identifier, VertexFormat> = HashBiMap.create()
    @JvmField
    val CODEC: Codec<VertexFormat> = Identifier.CODEC.xmap(
        { REGISTRY[it] },
        { REGISTRY.inverse()[it] }
    )

    @JvmField
    val ELEMENT_REGISTRY: BiMap<Identifier, VertexFormatElement> = HashBiMap.create()
    @JvmField
    val ELEMENT_CODEC: Codec<VertexFormatElement> = Identifier.CODEC.xmap(
        { ELEMENT_REGISTRY[it] },
        { ELEMENT_REGISTRY.inverse()[it] }
    )

    fun register(location: Identifier, format: VertexFormat) {
        REGISTRY[location] = format
    }

    fun register(location: Identifier, format: VertexFormatElement) {
        ELEMENT_REGISTRY[location] = format
    }

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

    /**
     * ```
     * Id: 0
     * Index: 0
     * Type: Float
     * Normalized: false
     * Count: 3
     * Mask: 1
     * Size Bytes: 12
     * ```
     */
    @JvmField
    val POSITION_ELEMENT = InternalUtil.INSTANCE.positionVertexElement
    /**
     * ```
     * Id: 1
     * Index: 0
     * Type: UByte
     * Normalized: false
     * Count: 4
     * Mask: 2
     * Size Bytes: 16
     * ```
     */
    @JvmField
    val COLOR_ELEMENT = InternalUtil.INSTANCE.colorVertexElement
    /**
     * ```
     * Id: 2
     * Index: 0
     * Type: Float
     * Normalized: false
     * Count: 2
     * Mask: 4
     * Size Bytes: 8
     * ```
     */
    @JvmField
    val TEXTURE_UV_ELEMENT = InternalUtil.INSTANCE.textureUVVertexElement
    /**
     * ```
     * Id: 3
     * Index: 1
     * Type: Short
     * Normalized: false
     * Count: 2
     * Mask: 8
     * Size Bytes: 4
     * ```
     */
    @JvmField
    val OVERLAY_UV_ELEMENT = InternalUtil.INSTANCE.overlayUVVertexElement
    /**
     * ```
     * Id: 4
     * Index: 2
     * Type: Short
     * Normalized: false
     * Count: 2
     * Mask: 16
     * Size Bytes: 4
     * ```
     */
    @JvmField
    val LIGHT_UV_ELEMENT = InternalUtil.INSTANCE.lightUVVertexElement
    /**
     * ```
     * Id: 5
     * Index: 0
     * Type: Byte
     * Normalized: true
     * Count: 3
     * Mask: 32
     * Size Bytes: 3
     * ```
     */
    @JvmField
    val NORMAL_ELEMENT = InternalUtil.INSTANCE.normalVertexElement

    init {
        register(Identifier.minecraft("block"), BLOCK)
        register(Identifier.minecraft("new_entity"), NEW_ENTITY)
        register(Identifier.minecraft("particle"), PARTICLE)
        register(Identifier.minecraft("position"), POSITION)
        register(Identifier.minecraft("position_color"), POSITION_COLOR)
        register(Identifier.minecraft("position_color_normal"), POSITION_COLOR_NORMAL)
        register(Identifier.minecraft("position_color_lightmap"), POSITION_COLOR_LIGHTMAP)
        register(Identifier.minecraft("position_tex"), POSITION_TEX)
        register(Identifier.minecraft("position_tex_color"), POSITION_TEX_COLOR)
        register(Identifier.minecraft("position_color_tex_lightmap"), POSITION_COLOR_TEX_LIGHTMAP)
        register(Identifier.minecraft("position_tex_lightmap_color"), POSITION_TEX_LIGHTMAP_COLOR)
        register(Identifier.minecraft("position_tex_color_normal"), POSITION_TEX_COLOR_NORMAL)

        register(Identifier.minecraft("position"), POSITION_ELEMENT)
        register(Identifier.minecraft("color"), COLOR_ELEMENT)
        register(Identifier.minecraft("texture_uv"), TEXTURE_UV_ELEMENT)
        register(Identifier.minecraft("overlay_uv"), OVERLAY_UV_ELEMENT)
        register(Identifier.minecraft("light_uv"), LIGHT_UV_ELEMENT)
        register(Identifier.minecraft("normal"), NORMAL_ELEMENT)
    }
}