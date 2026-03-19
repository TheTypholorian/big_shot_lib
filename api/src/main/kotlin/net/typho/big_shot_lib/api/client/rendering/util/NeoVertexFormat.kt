package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlDataType
import net.typho.big_shot_lib.api.util.*
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey

interface NeoVertexFormat : Iterable<NeoVertexFormat.Element> {
    val vertexSizeBytes: Int
    val elements: Array<Element>
    val elementNames: Array<String>
    val elementOffsets: IntArray

    fun getElementName(element: Element): String

    fun getElementOffset(element: Element): Int

    fun initVertexArrayState()

    override fun iterator() = elements.iterator()

    companion object : BigShotCommonEntrypoint {
        val REGISTRY_KEY = NeoResourceKey.registry<NeoVertexFormat>(BigShotApi.id("vertex_formats"))
        var REGISTRY: NeoRegistry<NeoVertexFormat>? = null
            private set
        val CODEC: Codec<NeoVertexFormat> = NeoResourceKey.codec(REGISTRY_KEY).xmap(
            { REGISTRY!!.get(it) },
            { REGISTRY!!.getKey(it) }
        )

        override fun registerRegistries(factory: RegistryFactory) {
            REGISTRY = factory.create(REGISTRY_KEY)
        }

        override fun registerContent(factory: RegistrationFactory) {
            val registrar = factory.begin(REGISTRY_KEY, NeoIdentifier.DEFAULT_NAMESPACE)
            registrar.register("blit_screen") { BLIT_SCREEN }
            registrar.register("block") { BLOCK }
            registrar.register("new_entity") { NEW_ENTITY }
            registrar.register("particle") { PARTICLE }
            registrar.register("position") { POSITION }
            registrar.register("position_color") { POSITION_COLOR }
            registrar.register("position_color_normal") { POSITION_COLOR_NORMAL }
            registrar.register("position_color_lightmap") { POSITION_COLOR_LIGHTMAP }
            registrar.register("position_tex") { POSITION_TEX }
            registrar.register("position_tex_color") { POSITION_TEX_COLOR }
            registrar.register("position_color_tex_lightmap") { POSITION_COLOR_TEX_LIGHTMAP }
            registrar.register("position_tex_lightmap_color") { POSITION_TEX_LIGHTMAP_COLOR }
            registrar.register("position_tex_color_normal") { POSITION_TEX_COLOR_NORMAL }
        }

        @JvmStatic
        fun provider() = this

        /**
         * - Position
         */
        val BLIT_SCREEN = WrapperUtil.INSTANCE.blitScreenVertexFormat()
        /**
         * - Position
         * - Color
         * - Texture UV
         * - Light UV
         * - Normal
         * - 1 byte padding
         */
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
        val NEW_ENTITY = WrapperUtil.INSTANCE.newEntityVertexFormat()
        /**
         * - Position
         * - Texture UV
         * - Color
         * - Light UV
         */
        val PARTICLE = WrapperUtil.INSTANCE.particleVertexFormat()
        /**
         * - Position
         */
        val POSITION = WrapperUtil.INSTANCE.positionVertexFormat()
        /**
         * - Position
         * - Color
         */
        val POSITION_COLOR = WrapperUtil.INSTANCE.positionColorVertexFormat()
        /**
         * - Position
         * - Color
         * - Normal
         * - 1 byte padding
         */
        val POSITION_COLOR_NORMAL = WrapperUtil.INSTANCE.positionColorNormalVertexFormat()
        /**
         * - Position
         * - Color
         * - Light UV
         */
        val POSITION_COLOR_LIGHTMAP = WrapperUtil.INSTANCE.positionColorLightVertexFormat()
        /**
         * - Position
         * - Texture UV
         */
        val POSITION_TEX = WrapperUtil.INSTANCE.positionTexVertexFormat()
        /**
         * - Position
         * - Texture UV
         * - Color
         */
        val POSITION_TEX_COLOR = WrapperUtil.INSTANCE.positionTexColorVertexFormat()
        /**
         * - Position
         * - Color
         * - Texture UV
         * - Light UV
         */
        val POSITION_COLOR_TEX_LIGHTMAP = WrapperUtil.INSTANCE.positionColorTexLightVertexFormat()
        /**
         * - Position
         * - Texture UV
         * - Light UV
         * - Color
         */
        val POSITION_TEX_LIGHTMAP_COLOR = WrapperUtil.INSTANCE.positionTexLightColorVertexFormat()
        /**
         * - Position
         * - Texture UV
         * - Color
         * - Normal
         * - 1 byte padding
         */
        val POSITION_TEX_COLOR_NORMAL = WrapperUtil.INSTANCE.positionTexColorNormalVertexFormat()

        @JvmStatic
        fun builder() = WrapperUtil.INSTANCE.createVertexFormatBuilder()
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