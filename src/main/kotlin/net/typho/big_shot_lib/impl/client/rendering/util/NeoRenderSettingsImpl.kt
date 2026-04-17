package net.typho.big_shot_lib.impl.client.rendering.util

//? if <1.21 {
/*import net.typho.big_shot_lib.mixin.impl.RenderTypeAccessor
*///? }

//? if <1.21.11 {
import net.minecraft.client.renderer.RenderType
//? } else {
/*import net.minecraft.client.renderer.rendertype.RenderType
*///? }

import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlDrawState
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderSettings
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.impl.util.getExtensionValue
import net.typho.big_shot_lib.mixin.impl.RenderTypeNameAccessor
import kotlin.jvm.optionals.getOrNull

data class NeoRenderSettingsImpl(
    @JvmField
    val renderType: RenderType
) : NeoRenderSettings {
    override val format: NeoVertexFormat = NeoVertexFormatImpl(renderType.format())
    override val mode: GlBeginMode = when (renderType.mode()) {
        VertexFormat.Mode.LINES, VertexFormat.Mode.DEBUG_LINES -> GlBeginMode.LINES
        //? if <1.21.11 {
        VertexFormat.Mode.LINE_STRIP -> GlBeginMode.LINE_STRIP
        //? }
        VertexFormat.Mode.DEBUG_LINE_STRIP -> GlBeginMode.LINE_STRIP
        VertexFormat.Mode.TRIANGLES -> GlBeginMode.TRIANGLES
        VertexFormat.Mode.TRIANGLE_STRIP -> GlBeginMode.TRIANGLE_STRIP
        VertexFormat.Mode.TRIANGLE_FAN -> GlBeginMode.TRIANGLE_FAN
        VertexFormat.Mode.QUADS -> GlBeginMode.QUADS
        //? if >=1.21.11 {
        /*VertexFormat.Mode.POINTS -> GlBeginMode.POINTS
        *///? }
    }
    override val defaultBufferSize: Int = renderType.bufferSize()
    override val affectsCrumbling: Boolean = renderType.affectsCrumbling()
    //? if <1.21 {
    /*override val sortOnUpload: Boolean = (renderType as RenderTypeAccessor).`big_shot_lib$getSortOnUpload`()
    *///? } else {
    override val sortOnUpload: Boolean = renderType.sortOnUpload()
    //? }
    override val outlineSettings: NeoRenderSettings? = renderType.outline().map { NeoRenderSettingsImpl(it) }.getOrNull()
    override val isOutline: Boolean = renderType.isOutline
    override val location: NeoIdentifier = NeoIdentifier((renderType as RenderTypeNameAccessor).`big_shot_lib$getName`())
    override val drawState: GlDrawState
        get() = (renderType as RenderType.CompositeRenderType).state().getExtensionValue()

    override fun bind(): BoundResource {
        //? if <1.21.10 {
        renderType.setupRenderState()

        return BoundResource {
            renderType.clearRenderState()
        }
        //? } else {
        /*TODO("Not yet implemented")
        *///? }
    }
}