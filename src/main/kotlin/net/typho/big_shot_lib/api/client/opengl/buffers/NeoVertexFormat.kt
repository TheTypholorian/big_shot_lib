package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.util.GlPrimitiveType
import net.typho.big_shot_lib.api.util.WrapperUtil

interface NeoVertexFormat : Iterable<NeoVertexFormat.Element> {
    val vertexSizeBytes: Int
    val elements: Array<Element>
    val elementNames: Array<String>
    val elementOffsets: IntArray

    fun getElementName(element: Element): String

    fun getElementOffset(element: Element): Int

    fun initVertexArrayState()

    override fun iterator() = elements.iterator()

    companion object {
        /**
         * - Position
         */
        @JvmField
        val BLIT_SCREEN = WrapperUtil.INSTANCE.blitScreenVertexFormat()
        /**
         * - Position
         * - Color
         * - Texture UV
         * - Light UV
         * - Normal
         * - 1 byte padding
         */
        @JvmField
        val BLOCK = WrapperUtil.INSTANCE.blockVertexFormat()
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
        val NEW_ENTITY = WrapperUtil.INSTANCE.newEntityVertexFormat()
        /**
         * - Position
         * - Texture UV
         * - Color
         * - Light UV
         */
        @JvmField
        val PARTICLE = WrapperUtil.INSTANCE.particleVertexFormat()
        /**
         * - Position
         */
        @JvmField
        val POSITION = WrapperUtil.INSTANCE.positionVertexFormat()
        /**
         * - Position
         * - Color
         */
        @JvmField
        val POSITION_COLOR = WrapperUtil.INSTANCE.positionColorVertexFormat()
        /**
         * - Position
         * - Color
         * - Normal
         * - 1 byte padding
         */
        @JvmField
        val POSITION_COLOR_NORMAL = WrapperUtil.INSTANCE.positionColorNormalVertexFormat()
        /**
         * - Position
         * - Color
         * - Light UV
         */
        @JvmField
        val POSITION_COLOR_LIGHTMAP = WrapperUtil.INSTANCE.positionColorLightVertexFormat()
        /**
         * - Position
         * - Texture UV
         */
        @JvmField
        val POSITION_TEX = WrapperUtil.INSTANCE.positionTexVertexFormat()
        /**
         * - Position
         * - Texture UV
         * - Color
         */
        @JvmField
        val POSITION_TEX_COLOR = WrapperUtil.INSTANCE.positionTexColorVertexFormat()
        /**
         * - Position
         * - Color
         * - Texture UV
         * - Light UV
         */
        @JvmField
        val POSITION_COLOR_TEX_LIGHTMAP = WrapperUtil.INSTANCE.positionColorTexLightVertexFormat()
        /**
         * - Position
         * - Texture UV
         * - Light UV
         * - Color
         */
        @JvmField
        val POSITION_TEX_LIGHTMAP_COLOR = WrapperUtil.INSTANCE.positionTexLightColorVertexFormat()
        /**
         * - Position
         * - Texture UV
         * - Color
         * - Normal
         * - 1 byte padding
         */
        @JvmField
        val POSITION_TEX_COLOR_NORMAL = WrapperUtil.INSTANCE.positionTexColorNormalVertexFormat()

        @JvmStatic
        fun builder() = WrapperUtil.INSTANCE.createVertexFormatBuilder()
    }

    interface Element {
        val id: Int
        val index: Int
        val type: GlPrimitiveType
        val normalized: Boolean?
        val count: Int
        val mask: Int
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
            val POSITION = WrapperUtil.INSTANCE.positionVertexElement()
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
            val COLOR = WrapperUtil.INSTANCE.colorVertexElement()
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
            val TEXTURE_UV = WrapperUtil.INSTANCE.textureUVVertexElement()
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
            val OVERLAY_UV = WrapperUtil.INSTANCE.overlayUVVertexElement()
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
            val LIGHT_UV = WrapperUtil.INSTANCE.lightUVVertexElement()
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
            val NORMAL = WrapperUtil.INSTANCE.normalVertexElement()
        }
    }

    interface Builder {
        fun add(name: String, element: Element): Builder

        fun padding(bytes: Int): Builder

        fun build(): NeoVertexFormat
    }
}