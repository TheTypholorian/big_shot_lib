package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.client.renderer.RenderType
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface NeoRenderSettings : NamedResource {
    val format: NeoVertexFormat
    val mode: GlBeginMode
    val defaultBufferSize: Int
    /**
     * If objects with these render settings should be included in the block breaking overlay (ex. if the material is a physical, solid substance)
     */
    val affectsCrumbling: Boolean
    val sortOnUpload: Boolean
    val outlineSettings: NeoRenderSettings?
    val isOutline: Boolean

    fun bind(): Bound

    interface Bound : BoundResource {
        val settings: NeoRenderSettings
    }

    data class Basic(
        override val location: NeoIdentifier,
        override val format: NeoVertexFormat,
        override val mode: GlBeginMode = GlBeginMode.QUADS,
        override val defaultBufferSize: Int = RenderType.SMALL_BUFFER_SIZE,
        override val affectsCrumbling: Boolean = true,
        override val sortOnUpload: Boolean = false,
        override val outlineSettings: NeoRenderSettings? = null,
        override val isOutline: Boolean = false
    ) : NeoRenderSettings {
        override fun bind(): Bound {
        }
    }

    companion object {
        @JvmStatic
        val BUILTINS = Builtins::class.loadService()
    }

    interface Builtins {
        val solid: NeoRenderSettings
        val cutout: NeoRenderSettings
        val cutoutMipped: NeoRenderSettings
        val translucent: NeoRenderSettings
        val translucentMovingBlock: NeoRenderSettings
        val leash: NeoRenderSettings
        val waterMask: NeoRenderSettings
        val armorEntityGlint: NeoRenderSettings
        val glintTranslucent: NeoRenderSettings
        val glint: NeoRenderSettings
        val entityGlint: NeoRenderSettings
        val textBackground: NeoRenderSettings
        val textBackgroundSeeThrough: NeoRenderSettings
        val lightning: NeoRenderSettings
        val dragonRays: NeoRenderSettings
        val dragonRaysDepth: NeoRenderSettings
        val tripwire: NeoRenderSettings
        val endPortal: NeoRenderSettings
        val endGateway: NeoRenderSettings
        val lines: NeoRenderSettings
        val lineStrip: NeoRenderSettings
        val debugFilledBox: NeoRenderSettings
        val debugQuads: NeoRenderSettings
        val debugStructureQuads: NeoRenderSettings
        val debugSectionQuads: NeoRenderSettings

        fun armorCutoutNoCull(texture: NeoIdentifier): NeoRenderSettings

        fun entitySolid(texture: NeoIdentifier): NeoRenderSettings

        fun entityCutout(texture: NeoIdentifier): NeoRenderSettings

        fun entityCutoutNoCull(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderSettings

        fun entityCutoutNoCullZOffset(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderSettings

        fun itemEntityTranslucentCull(texture: NeoIdentifier): NeoRenderSettings

        fun entityTranslucent(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderSettings

        fun entityTranslucentEmissive(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderSettings

        fun entitySmoothCutout(texture: NeoIdentifier): NeoRenderSettings

        fun beaconBeam(texture: NeoIdentifier, affectsOutline: Boolean): NeoRenderSettings

        fun entityDecal(texture: NeoIdentifier): NeoRenderSettings

        fun entityNoOutline(texture: NeoIdentifier): NeoRenderSettings

        fun entityShadow(texture: NeoIdentifier): NeoRenderSettings

        fun dragonExplosionAlpha(texture: NeoIdentifier): NeoRenderSettings

        fun eyes(texture: NeoIdentifier): NeoRenderSettings

        fun crumbling(texture: NeoIdentifier): NeoRenderSettings

        fun text(texture: NeoIdentifier): NeoRenderSettings

        fun textIntensity(texture: NeoIdentifier): NeoRenderSettings

        fun textPolygonOffset(texture: NeoIdentifier): NeoRenderSettings

        fun textIntensityPolygonOffset(texture: NeoIdentifier): NeoRenderSettings

        fun textSeeThrough(texture: NeoIdentifier): NeoRenderSettings

        fun textIntensitySeeThrough(texture: NeoIdentifier): NeoRenderSettings

        fun debugLineStrip(lineWidth: Double): NeoRenderSettings
    }
}