package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlDataType
import net.typho.big_shot_lib.api.util.*
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey.Companion.lookupOrThrow

interface NeoVertexFormat : Iterable<NeoVertexFormat.Element> {
    val vertexSizeBytes: Int
    val elements: Array<Element>
    val elementNames: Array<String>

    fun getElementName(element: Element): String

    fun getElementOffset(element: Element): Int

    fun initVertexArrayState()

    override fun iterator() = elements.iterator()

    companion object : BigShotCommonEntrypoint(BigShotApi.MOD_ID) {
        var REGISTRY = createRegistry<NeoVertexFormat>(BigShotApi.id("vertex_formats"))
        val CODEC: Codec<NeoVertexFormat> = NeoResourceKey.codec(REGISTRY).xmap(
            { REGISTRY.lookupOrThrow().get(it) },
            { REGISTRY.lookupOrThrow().getKey(it) }
        )

        /**
         * - Position
         */
        val BLIT_SCREEN by register(REGISTRY, NeoIdentifier("blit_screen"), InternalUtil.INSTANCE.blitScreenVertexFormat)
        /**
         * - Position
         * - Color
         * - Texture UV
         * - Light UV
         * - Normal
         * - 1 byte padding
         */
        val BLOCK by register(REGISTRY, NeoIdentifier("block"), InternalUtil.INSTANCE.blockVertexFormat)
        /**
         * - Position
         * - Color
         * - Texture UV
         * - Overlay UV
         * - Light UV
         * - Normal
         * - 1 byte padding
         */
        val NEW_ENTITY by register(REGISTRY, NeoIdentifier("new_entity"), InternalUtil.INSTANCE.newEntityVertexFormat)
        /**
         * - Position
         * - Texture UV
         * - Color
         * - Light UV
         */
        val PARTICLE by register(REGISTRY, NeoIdentifier("particle"), InternalUtil.INSTANCE.particleVertexFormat)
        /**
         * - Position
         */
        val POSITION by register(REGISTRY, NeoIdentifier("position"), InternalUtil.INSTANCE.positionVertexFormat)
        /**
         * - Position
         * - Color
         */
        val POSITION_COLOR by register(REGISTRY, NeoIdentifier("position_color"), InternalUtil.INSTANCE.positionColorVertexFormat)
        /**
         * - Position
         * - Color
         * - Normal
         * - 1 byte padding
         */
        val POSITION_COLOR_NORMAL by register(REGISTRY, NeoIdentifier("position_color_normal"), InternalUtil.INSTANCE.positionColorNormalVertexFormat)
        /**
         * - Position
         * - Color
         * - Light UV
         */
        val POSITION_COLOR_LIGHTMAP by register(REGISTRY, NeoIdentifier("position_color_lightmap"), InternalUtil.INSTANCE.positionColorLightVertexFormat)
        /**
         * - Position
         * - Texture UV
         */
        val POSITION_TEX by register(REGISTRY, NeoIdentifier("position_tex"), InternalUtil.INSTANCE.positionTexVertexFormat)
        /**
         * - Position
         * - Texture UV
         * - Color
         */
        val POSITION_TEX_COLOR by register(REGISTRY, NeoIdentifier("position_tex_color"), InternalUtil.INSTANCE.positionTexColorVertexFormat)
        /**
         * - Position
         * - Color
         * - Texture UV
         * - Light UV
         */
        val POSITION_COLOR_TEX_LIGHTMAP by register(REGISTRY, NeoIdentifier("position_color_tex_lightmap"), InternalUtil.INSTANCE.positionColorTexLightVertexFormat)
        /**
         * - Position
         * - Texture UV
         * - Light UV
         * - Color
         */
        val POSITION_TEX_LIGHTMAP_COLOR by register(REGISTRY, NeoIdentifier("position_tex_lightmap_color"), InternalUtil.INSTANCE.positionTexLightColorVertexFormat)
        /**
         * - Position
         * - Texture UV
         * - Color
         * - Normal
         * - 1 byte padding
         */
        val POSITION_TEX_COLOR_NORMAL by register(REGISTRY, NeoIdentifier("position_tex_color_normal"), InternalUtil.INSTANCE.positionTexColorNormalVertexFormat)

        @JvmStatic
        fun builder() = InternalUtil.INSTANCE.createVertexFormatBuilder()

        override fun onInitialize() {
        }
    }

    interface Element {
        val index: Int
        val type: GlDataType
        val normalized: Boolean?
        val count: Int
        val sizeBytes: Int

        fun vertexAttribPointer(index: Int, offset: Long, stride: Int)

        companion object {
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
            val POSITION = InternalUtil.INSTANCE.positionVertexElement
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
            val COLOR = InternalUtil.INSTANCE.colorVertexElement
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
            val TEXTURE_UV = InternalUtil.INSTANCE.textureUVVertexElement
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
            val OVERLAY_UV = InternalUtil.INSTANCE.overlayUVVertexElement
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
            val LIGHT_UV = InternalUtil.INSTANCE.lightUVVertexElement
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
            val NORMAL = InternalUtil.INSTANCE.normalVertexElement
        }
    }

    interface Builder {
        fun add(name: String, element: Element): Builder

        fun padding(bytes: Int): Builder

        fun build(): NeoVertexFormat
    }
}