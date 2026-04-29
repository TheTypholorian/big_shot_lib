package net.typho.big_shot_lib.api

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlDataType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.util.NeoAtlas
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey

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

    fun createVertexFormatElement(index: Int, type: GlDataType, normalized: Boolean?, count: Int): NeoVertexFormat.Element

    fun getTexture(location: NeoIdentifier): GlTexture2D?

    fun getAtlas(location: NeoIdentifier): NeoAtlas?

    fun transformNormal(pose: PoseStack.Pose, x: Float, y: Float, z: Float): IVec3<Float>

    fun <T : Any> getRegistry(key: NeoResourceKey<Registry<T>>): Registry<T>?

    fun mainWindowHandle(): Long

    fun onBind(program: GlProgram)

    fun onUnbind(program: GlProgram)

    companion object {
        val INSTANCE by lazy { InternalUtil::class.loadService() }
    }
}