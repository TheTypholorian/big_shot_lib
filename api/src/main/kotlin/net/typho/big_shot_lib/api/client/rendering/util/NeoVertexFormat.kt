package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlDataType
import net.typho.big_shot_lib.api.util.*

interface NeoVertexFormat : Iterable<NeoVertexFormat.Element> {
    val vertexSizeBytes: Int
    val elements: Array<Element>
    val elementNames: Array<String>

    fun getElementName(element: Element): String

    fun getElementOffset(element: Element): Int

    fun initVertexArrayState()

    override fun iterator() = elements.iterator()

    object Entrypoint : BigShotCommonEntrypoint(BigShotApi.MOD_ID) {
        internal val REGISTRY = createRegistry<NeoVertexFormat>(BigShotApi.id("vertex_formats"))

        override fun onInitialize() {
            register(REGISTRY, Identifier.minecraft("blit_screen"), InternalUtil.INSTANCE.blitScreenVertexFormat)
            register(REGISTRY, Identifier.minecraft("block"), InternalUtil.INSTANCE.blockVertexFormat)
            register(REGISTRY, Identifier.minecraft("new_entity"), InternalUtil.INSTANCE.newEntityVertexFormat)
            register(REGISTRY, Identifier.minecraft("particle"), InternalUtil.INSTANCE.particleVertexFormat)
            register(REGISTRY, Identifier.minecraft("position"), InternalUtil.INSTANCE.positionVertexFormat)
            register(REGISTRY, Identifier.minecraft("position_color"), InternalUtil.INSTANCE.positionColorVertexFormat)
            register(REGISTRY, Identifier.minecraft("position_color_normal"), InternalUtil.INSTANCE.positionColorNormalVertexFormat)
            register(REGISTRY, Identifier.minecraft("position_color_lightmap"), InternalUtil.INSTANCE.positionColorLightVertexFormat)
            register(REGISTRY, Identifier.minecraft("position_tex"), InternalUtil.INSTANCE.positionTexVertexFormat)
            register(REGISTRY, Identifier.minecraft("position_tex_color"), InternalUtil.INSTANCE.positionTexColorVertexFormat)
            register(REGISTRY, Identifier.minecraft("position_color_tex_lightmap"), InternalUtil.INSTANCE.positionColorTexLightVertexFormat)
            register(REGISTRY, Identifier.minecraft("position_tex_lightmap_color"), InternalUtil.INSTANCE.positionTexLightColorVertexFormat)
            register(REGISTRY, Identifier.minecraft("position_tex_color_normal"), InternalUtil.INSTANCE.positionTexColorNormalVertexFormat)
        }
    }

    companion object {
        @JvmStatic
        fun builder() = InternalUtil.INSTANCE.createVertexFormatBuilder()
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