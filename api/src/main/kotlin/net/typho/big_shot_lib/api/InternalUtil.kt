package net.typho.big_shot_lib.api

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlDrawState
import net.typho.big_shot_lib.api.client.rendering.util.NeoAtlas
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

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

    fun getTexture(location: Identifier): GlTexture2D?

    fun getAtlas(location: Identifier): NeoAtlas?

    fun transformNormal(pose: PoseStack.Pose, x: Float, y: Float, z: Float): IVec3<Float>

    fun <T : Any> getRegistry(key: ResourceKey<Registry<T>>): Registry<T>?

    fun mainWindowHandle(): Long

    fun createShader(location: Identifier, type: GlShaderType, glId: Int): GlShader

    fun createProgram(location: Identifier, format: NeoVertexFormat, glId: Int): GlProgram

    fun createRenderType(
        location: Identifier,
        format: NeoVertexFormat,
        drawState: GlDrawState,
        defaultBufferSize: Int,
        mode: GlBeginMode,
        affectsCrumbling: Boolean,
        sortOnUpload: Boolean,
        isOutline: Boolean
    ): NeoRenderType

    companion object {
        @JvmStatic
        val INSTANCE by lazy { InternalUtil::class.loadService() }
    }
}