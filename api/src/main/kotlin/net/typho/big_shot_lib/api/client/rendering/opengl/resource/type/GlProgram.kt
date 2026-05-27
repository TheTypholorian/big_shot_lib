package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.error.ShaderLinkException
import net.typho.big_shot_lib.api.error.ShaderValidationException
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.util.resource.NamedResource

interface GlProgram : NamedResource, GlResource, UniformOutput {
    val format: NeoVertexFormat

    fun use() {
        NeoGlStateManager.INSTANCE.program = glId
    }

    fun attach(shader: GlShader)

    fun detach(shader: GlShader)

    fun getInfoLog(): String

    fun link(): Boolean

    fun validate(): Boolean

    fun linkOrThrow(onError: (log: String) -> Unit = { throw ShaderLinkException("Error linking program $location:\n$it") }) {
        if (!link()) {
            onError(getInfoLog())
        }
    }

    fun validateOrThrow(onError: (log: String) -> Unit = { throw ShaderValidationException("Invalid program $location:\n$it") }) {
        if (!validate()) {
            onError(getInfoLog())
        }
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        @JvmName("create")
        operator fun invoke(location: Identifier, format: VertexFormat, glId: Int = GlResourceType.PROGRAM.create()): GlProgram = InternalUtil.INSTANCE.createProgram(
            location,
            format,
            glId,
        )

        @JvmStatic
        operator fun get(location: Identifier): GlProgram? = InternalUtil.INSTANCE.getProgram(location)

        @JvmStatic
        fun getOrThrow(location: Identifier): GlProgram = get(location) ?: throw NullPointerException("No shader program $location")

        @JvmField
        val BUILTINS = Builtins::class.loadService()
    }

    interface Builtins {
        val position: GlProgram
        val positionColor: GlProgram
        val positonTex: GlProgram
        val positonTexColor: GlProgram
        val particle: GlProgram
        val positionColorLightmap: GlProgram
        val positionColorTexLightmap: GlProgram
        val solid: GlProgram
        val cutoutMipped: GlProgram
        val cutout: GlProgram
        val translucent: GlProgram
        val translucentMovingBlock: GlProgram
        val armorCutoutNoCull: GlProgram
        val entitySolid: GlProgram
        val entityCutout: GlProgram
        val entityCutoutNoCull: GlProgram
        val entityCutoutNoCullZOffset: GlProgram
        val itemEntityTranslucentCull: GlProgram
        val entityTranslucentCull: GlProgram
        val entityTranslucent: GlProgram
        val entityTranslucentEmissive: GlProgram
        val entitySmoothCutout: GlProgram
        val beaconBeam: GlProgram
        val entityDecal: GlProgram
        val entityNoOutline: GlProgram
        val entityShadow: GlProgram
        val entityAlpha: GlProgram
        val eyes: GlProgram
        val energySwirl: GlProgram
        val breezeWind: GlProgram
        val leash: GlProgram
        val waterMask: GlProgram
        val outline: GlProgram
        val armorGlint: GlProgram
        val armorEntityGlint: GlProgram
        val glintTranslucent: GlProgram
        val glint: GlProgram
        val glintDirect: GlProgram
        val entityGlint: GlProgram
        val entityGlintDirect: GlProgram
        val text: GlProgram
        val textBackground: GlProgram
        val textIntensity: GlProgram
        val textSeeThrough: GlProgram
        val textBackgroundSeeThrough: GlProgram
        val textIntensitySeeThrough: GlProgram
        val lightning: GlProgram
        val tripwire: GlProgram
        val endPortal: GlProgram
        val endGateway: GlProgram
        val clouds: GlProgram
        val lines: GlProgram
        val crumbling: GlProgram
        val gui: GlProgram
        val guiOverlay: GlProgram
        val guiTextHighlight: GlProgram
        val guiGhostRecipeOverlay: GlProgram
    }
}