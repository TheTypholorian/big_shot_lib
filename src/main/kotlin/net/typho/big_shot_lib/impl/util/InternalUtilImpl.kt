package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import com.mojang.serialization.DataResult
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.util.NeoBufferBuilder
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.impl.client.rendering.util.AutoSizeNeoBufferBuilderImpl
import net.typho.big_shot_lib.impl.client.rendering.util.KnownSizeNeoBufferBuilderImpl
import net.typho.big_shot_lib.impl.client.rendering.util.NeoVertexFormatImpl

object InternalUtilImpl : InternalUtil {
    override fun createVertexFormatBuilder(): NeoVertexFormat.Builder {
        return NeoVertexFormatImpl.BuilderImpl()
    }

    override val positionVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.POSITION)
        //? } else {
        /*get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_POSITION)
        *///? }
    override val colorVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.COLOR)
        //? } else {
        /*get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_COLOR)
        *///? }
    override val textureUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV0)
        //? } else {
        /*get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_UV0)
        *///? }
    override val overlayUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV1)
        //? } else {
        /*get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_UV1)
        *///? }
    override val lightUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV2)
        //? } else {
        /*get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_UV2)
        *///? }
    override val normalVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.NORMAL)
        //? } else {
        /*get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_NORMAL)
        *///? }

    //? if <1.21.9 {
    /*override val blitScreenVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.BLIT_SCREEN)
    *///? } else {
    override val blitScreenVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION)
    //? }
    override val blockVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.BLOCK)
    override val newEntityVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.NEW_ENTITY)
    override val particleVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.PARTICLE)
    override val positionVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION)
    override val positionColorVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR)
    override val positionColorNormalVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR_NORMAL)
    override val positionColorLightVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR_LIGHTMAP)
    override val positionTexVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX)
    override val positionTexColorVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX_COLOR)
    override val positionColorTexLightVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
    override val positionTexLightColorVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR)
    override val positionTexColorNormalVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL)

    override fun createBufferBuilder(
        format: NeoVertexFormat,
        mode: GlBeginMode,
        numVertices: Int?
    ): NeoBufferBuilder {
        return if (numVertices == null)
            AutoSizeNeoBufferBuilderImpl(format, mode)
        else
            KnownSizeNeoBufferBuilderImpl(format, mode, numVertices)
    }

    override fun <R> dataResultError(message: () -> String): DataResult<R> {
        //? if >=1.19.4 {
        return DataResult.error(message)
        //? } else {
        /*return DataResult.error(message())
        *///? }
    }
}