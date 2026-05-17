package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey.Companion.lookupOrThrow

object NeoVertexFormats {
    @JvmField
    val REGISTRY = NeoVertexFormat.Entrypoint.REGISTRY
    @JvmField
    val CODEC: Codec<NeoVertexFormat> = NeoResourceKey.codec(REGISTRY).xmap(
        { REGISTRY.lookupOrThrow().get(it) },
        { REGISTRY.lookupOrThrow().getKey(it) }
    )

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