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
        @JvmField
        val BLIT_SCREEN = WrapperUtil.INSTANCE.blitScreenVertexFormat()
        @JvmField
        val BLOCK = WrapperUtil.INSTANCE.blockVertexFormat()
        @JvmField
        val NEW_ENTITY = WrapperUtil.INSTANCE.newEntityVertexFormat()
        @JvmField
        val PARTICLE = WrapperUtil.INSTANCE.particleVertexFormat()
        @JvmField
        val POSITION = WrapperUtil.INSTANCE.positionVertexFormat()
        @JvmField
        val POSITION_COLOR = WrapperUtil.INSTANCE.positionColorVertexFormat()
        @JvmField
        val POSITION_COLOR_NORMAL = WrapperUtil.INSTANCE.positionColorNormalVertexFormat()
        @JvmField
        val POSITION_COLOR_LIGHTMAP = WrapperUtil.INSTANCE.positionColorLightVertexFormat()
        @JvmField
        val POSITION_TEX = WrapperUtil.INSTANCE.positionTexVertexFormat()
        @JvmField
        val POSITION_TEX_COLOR = WrapperUtil.INSTANCE.positionTexColorVertexFormat()
        @JvmField
        val POSITION_COLOR_TEX_LIGHTMAP = WrapperUtil.INSTANCE.positionColorTexLightVertexFormat()
        @JvmField
        val POSITION_TEX_LIGHTMAP_COLOR = WrapperUtil.INSTANCE.positionTexLightColorVertexFormat()
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
            @JvmField
            val POSITION = WrapperUtil.INSTANCE.positionVertexElement()
            @JvmField
            val COLOR = WrapperUtil.INSTANCE.colorVertexElement()
            @JvmField
            val TEXTURE_UV = WrapperUtil.INSTANCE.textureUVVertexElement()
            @JvmField
            val OVERLAY_UV = WrapperUtil.INSTANCE.overlayUVVertexElement()
            @JvmField
            val LIGHT_UV = WrapperUtil.INSTANCE.lightUVVertexElement()
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