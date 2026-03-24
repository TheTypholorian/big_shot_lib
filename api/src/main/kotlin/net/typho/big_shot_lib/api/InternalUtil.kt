package net.typho.big_shot_lib.api

import com.mojang.serialization.DataResult
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.util.NeoBufferBuilder
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat

interface InternalUtil {
    val positionVertexElement: NeoVertexFormat.Element
    val colorVertexElement: NeoVertexFormat.Element
    val textureUVVertexElement: NeoVertexFormat.Element
    val overlayUVVertexElement: NeoVertexFormat.Element
    val lightUVVertexElement: NeoVertexFormat.Element
    val normalVertexElement: NeoVertexFormat.Element
    val blitScreenVertexFormat: NeoVertexFormat
    val blockVertexFormat: NeoVertexFormat
    val newEntityVertexFormat: NeoVertexFormat
    val particleVertexFormat: NeoVertexFormat
    val positionVertexFormat: NeoVertexFormat
    val positionColorVertexFormat: NeoVertexFormat
    val positionColorNormalVertexFormat: NeoVertexFormat
    val positionColorLightVertexFormat: NeoVertexFormat
    val positionTexVertexFormat: NeoVertexFormat
    val positionTexColorVertexFormat: NeoVertexFormat
    val positionColorTexLightVertexFormat: NeoVertexFormat
    val positionTexLightColorVertexFormat: NeoVertexFormat
    val positionTexColorNormalVertexFormat: NeoVertexFormat

    fun createVertexFormatBuilder(): NeoVertexFormat.Builder

    fun createBufferBuilder(format: NeoVertexFormat, mode: GlBeginMode, numVertices: Int? = null): NeoBufferBuilder

    fun <R> dataResultError(message: () -> String): DataResult<R>

    companion object {
        internal val INSTANCE = InternalUtil::class.loadService()
    }
}