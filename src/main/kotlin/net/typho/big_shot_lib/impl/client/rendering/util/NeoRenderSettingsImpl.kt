package net.typho.big_shot_lib.impl.client.rendering.util

//? if <1.21 {
/*import net.typho.big_shot_lib.impl.mixin.RenderTypeAccessor
*///? }

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderType
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlDrawState
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderSettings
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.impl.mixin.RenderStateShardAccessor
import kotlin.jvm.optionals.getOrNull

class NeoRenderSettingsImpl(
    @JvmField
    val renderType: RenderType
) : NeoRenderSettings {
    override val format: NeoVertexFormat = NeoVertexFormatImpl(renderType.format())
    override val mode: GlBeginMode = when (renderType.mode()) {
        VertexFormat.Mode.LINES, VertexFormat.Mode.DEBUG_LINES -> GlBeginMode.LINES
        VertexFormat.Mode.LINE_STRIP, VertexFormat.Mode.DEBUG_LINE_STRIP -> GlBeginMode.LINE_STRIP
        VertexFormat.Mode.TRIANGLES -> GlBeginMode.TRIANGLES
        VertexFormat.Mode.TRIANGLE_STRIP -> GlBeginMode.TRIANGLE_STRIP
        VertexFormat.Mode.TRIANGLE_FAN -> GlBeginMode.TRIANGLE_FAN
        VertexFormat.Mode.QUADS -> GlBeginMode.QUADS
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
    override val location: NeoIdentifier = NeoIdentifier((renderType as RenderStateShardAccessor).`big_shot_lib$getName`())
    override val drawState: GlDrawState
        get() = TODO("Not yet implemented")

    override fun bind(): BoundResource {
        renderType.setupRenderState()

        return BoundResource {
            renderType.clearRenderState()
        }
    }
}