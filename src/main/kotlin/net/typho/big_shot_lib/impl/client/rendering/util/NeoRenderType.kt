package net.typho.big_shot_lib.impl.client.rendering.util

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderType
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderSettings

open class NeoRenderType(
    @JvmField
    val settings: NeoRenderSettings,
    @JvmField
    protected val boundStack: MutableList<BoundResource> = arrayListOf()
) : RenderType(
    settings.location.toString(),
    (settings.format as NeoVertexFormatImpl).inner,
    when (settings.mode) {
        GlBeginMode.POINTS -> TODO("Properly support points begin mode")
        GlBeginMode.LINES -> VertexFormat.Mode.LINES
        GlBeginMode.LINE_LOOP -> TODO("Properly support line loop begin mode")
        GlBeginMode.LINE_STRIP -> VertexFormat.Mode.LINE_STRIP
        GlBeginMode.TRIANGLES -> VertexFormat.Mode.TRIANGLES
        GlBeginMode.TRIANGLE_STRIP -> VertexFormat.Mode.TRIANGLE_STRIP
        GlBeginMode.TRIANGLE_FAN -> VertexFormat.Mode.TRIANGLE_FAN
        GlBeginMode.QUADS -> VertexFormat.Mode.QUADS
    },
    settings.defaultBufferSize,
    settings.affectsCrumbling,
    settings.sortOnUpload,
    { boundStack.add(settings.bind()) },
    { boundStack.removeLast().unbind() }
)