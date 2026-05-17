package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlDrawState
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource

interface NeoRenderType : MaybeNamedResource {
    val format: NeoVertexFormat
    val mode: GlBeginMode
    val defaultBufferSize: Int
    /**
     * If objects with these render settings should be included in the block breaking overlay (ex. if the material is a physical, solid substance)
     */
    val affectsCrumbling: Boolean
    val sortOnUpload: Boolean
    val outlineSettings: NeoRenderType?
    val isOutline: Boolean
    val drawState: GlDrawState

    fun bind(): BoundResource = drawState.bind()

    companion object {
        @JvmField
        val BUILTINS = Builtins::class.loadService()

        @JvmStatic
        @JvmOverloads
        fun create(
            location: Identifier,
            format: NeoVertexFormat,
            drawState: GlDrawState,
            defaultBufferSize: Int = 786432,
            mode: GlBeginMode = GlBeginMode.QUADS,
            affectsCrumbling: Boolean = true,
            sortOnUpload: Boolean = false,
            isOutline: Boolean = false
        ): NeoRenderType = InternalUtil.INSTANCE.createRenderType(
            location,
            format,
            drawState,
            defaultBufferSize,
            mode,
            affectsCrumbling,
            sortOnUpload,
            isOutline
        )
    }

    interface Builtins {
        val solid: NeoRenderType
        val cutout: NeoRenderType
        val cutoutMipped: NeoRenderType
        val translucent: NeoRenderType
        val translucentMovingBlock: NeoRenderType
        val leash: NeoRenderType
        val waterMask: NeoRenderType
        val armorEntityGlint: NeoRenderType
        val glintTranslucent: NeoRenderType
        val glint: NeoRenderType
        val entityGlint: NeoRenderType
        val textBackground: NeoRenderType
        val textBackgroundSeeThrough: NeoRenderType
        val lightning: NeoRenderType
        val tripwire: NeoRenderType
        val endPortal: NeoRenderType
        val endGateway: NeoRenderType
        val lines: NeoRenderType
        val lineStrip: NeoRenderType
        val debugFilledBox: NeoRenderType
        val debugQuads: NeoRenderType
        val debugSectionQuads: NeoRenderType

        fun armorCutoutNoCull(texture: Identifier): NeoRenderType

        fun entitySolid(texture: Identifier): NeoRenderType

        fun entityCutout(texture: Identifier): NeoRenderType

        fun entityCutoutNoCull(texture: Identifier, affectsOutline: Boolean): NeoRenderType

        fun entityCutoutNoCullZOffset(texture: Identifier, affectsOutline: Boolean): NeoRenderType

        fun itemEntityTranslucentCull(texture: Identifier): NeoRenderType

        fun entityTranslucent(texture: Identifier, affectsOutline: Boolean): NeoRenderType

        fun entityTranslucentEmissive(texture: Identifier, affectsOutline: Boolean): NeoRenderType

        fun entitySmoothCutout(texture: Identifier): NeoRenderType

        fun beaconBeam(texture: Identifier, affectsOutline: Boolean): NeoRenderType

        fun entityDecal(texture: Identifier): NeoRenderType

        fun entityNoOutline(texture: Identifier): NeoRenderType

        fun entityShadow(texture: Identifier): NeoRenderType

        fun dragonExplosionAlpha(texture: Identifier): NeoRenderType

        fun eyes(texture: Identifier): NeoRenderType

        fun crumbling(texture: Identifier): NeoRenderType

        fun text(texture: Identifier): NeoRenderType

        fun textIntensity(texture: Identifier): NeoRenderType

        fun textPolygonOffset(texture: Identifier): NeoRenderType

        fun textIntensityPolygonOffset(texture: Identifier): NeoRenderType

        fun textSeeThrough(texture: Identifier): NeoRenderType

        fun textIntensitySeeThrough(texture: Identifier): NeoRenderType

        fun debugLineStrip(lineWidth: Double): NeoRenderType
    }
}