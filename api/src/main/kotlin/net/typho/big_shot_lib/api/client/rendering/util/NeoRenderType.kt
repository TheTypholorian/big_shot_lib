package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.client.renderer.RenderType
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlDrawState
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.impl.util.ImmutableExtension

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

    interface ExtensionValue : NeoRenderType, ImmutableExtension<RenderType>

    companion object {
        @JvmField
        val BUILTINS = Builtins::class.loadService()

        @JvmStatic
        @JvmOverloads
        fun create(
            location: NeoIdentifier,
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

        fun armorCutoutNoCull(texture: NeoIdentifier): NeoRenderType

        fun entitySolid(texture: NeoIdentifier): NeoRenderType

        fun entityCutout(texture: NeoIdentifier): NeoRenderType

        fun entityCutoutNoCull(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderType

        fun entityCutoutNoCullZOffset(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderType

        fun itemEntityTranslucentCull(texture: NeoIdentifier): NeoRenderType

        fun entityTranslucent(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderType

        fun entityTranslucentEmissive(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderType

        fun entitySmoothCutout(texture: NeoIdentifier): NeoRenderType

        fun beaconBeam(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderType

        fun entityDecal(texture: NeoIdentifier): NeoRenderType

        fun entityNoOutline(texture: NeoIdentifier): NeoRenderType

        fun entityShadow(texture: NeoIdentifier): NeoRenderType

        fun dragonExplosionAlpha(texture: NeoIdentifier): NeoRenderType

        fun eyes(texture: NeoIdentifier): NeoRenderType

        fun crumbling(texture: NeoIdentifier): NeoRenderType

        fun text(texture: NeoIdentifier): NeoRenderType

        fun textIntensity(texture: NeoIdentifier): NeoRenderType

        fun textPolygonOffset(texture: NeoIdentifier): NeoRenderType

        fun textIntensityPolygonOffset(texture: NeoIdentifier): NeoRenderType

        fun textSeeThrough(texture: NeoIdentifier): NeoRenderType

        fun textIntensitySeeThrough(texture: NeoIdentifier): NeoRenderType

        fun debugLineStrip(lineWidth: Double): NeoRenderType
    }
}