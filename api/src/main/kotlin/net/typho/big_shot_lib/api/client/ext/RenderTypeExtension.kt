package net.typho.big_shot_lib.api.client.ext

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.common.GpuDrawSettings
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource

interface RenderTypeExtension : GpuDrawSettings, MaybeNamedResource {
    val format: VertexFormat
    val defaultBufferSize: Int
    /**
     * If objects with these render settings should be included in the block breaking overlay (ex. if the material is a physical, solid substance)
     */
    val affectsCrumbling: Boolean
    val sortOnUpload: Boolean
    val outlineSettings: RenderTypeExtension?
    val isOutline: Boolean

    companion object {
        @JvmField
        @get:JvmName("getBuiltins")
        val BUILTINS = Builtins::class.loadService()
    }

    interface Builtins {
        val solid: RenderTypeExtension
        val cutout: RenderTypeExtension
        val cutoutMipped: RenderTypeExtension
        val translucent: RenderTypeExtension
        val translucentMovingBlock: RenderTypeExtension
        val leash: RenderTypeExtension
        val waterMask: RenderTypeExtension
        val armorEntityGlint: RenderTypeExtension
        val glintTranslucent: RenderTypeExtension
        val glint: RenderTypeExtension
        val entityGlint: RenderTypeExtension
        val textBackground: RenderTypeExtension
        val textBackgroundSeeThrough: RenderTypeExtension
        val lightning: RenderTypeExtension
        val tripwire: RenderTypeExtension
        val endPortal: RenderTypeExtension
        val endGateway: RenderTypeExtension
        val lines: RenderTypeExtension
        val lineStrip: RenderTypeExtension
        val debugFilledBox: RenderTypeExtension
        val debugQuads: RenderTypeExtension
        val debugSectionQuads: RenderTypeExtension

        fun armorCutoutNoCull(texture: Identifier): RenderTypeExtension

        fun entitySolid(texture: Identifier): RenderTypeExtension

        fun entityCutout(texture: Identifier): RenderTypeExtension

        fun entityCutoutNoCull(texture: Identifier, affectsOutline: Boolean): RenderTypeExtension

        fun entityCutoutNoCullZOffset(texture: Identifier, affectsOutline: Boolean): RenderTypeExtension

        fun itemEntityTranslucentCull(texture: Identifier): RenderTypeExtension

        fun entityTranslucent(texture: Identifier, affectsOutline: Boolean): RenderTypeExtension

        fun entityTranslucentEmissive(texture: Identifier, affectsOutline: Boolean): RenderTypeExtension

        fun entitySmoothCutout(texture: Identifier): RenderTypeExtension

        fun beaconBeam(texture: Identifier, affectsOutline: Boolean): RenderTypeExtension

        fun entityDecal(texture: Identifier): RenderTypeExtension

        fun entityNoOutline(texture: Identifier): RenderTypeExtension

        fun entityShadow(texture: Identifier): RenderTypeExtension

        fun dragonExplosionAlpha(texture: Identifier): RenderTypeExtension

        fun eyes(texture: Identifier): RenderTypeExtension

        fun crumbling(texture: Identifier): RenderTypeExtension

        fun text(texture: Identifier): RenderTypeExtension

        fun textIntensity(texture: Identifier): RenderTypeExtension

        fun textPolygonOffset(texture: Identifier): RenderTypeExtension

        fun textIntensityPolygonOffset(texture: Identifier): RenderTypeExtension

        fun textSeeThrough(texture: Identifier): RenderTypeExtension

        fun textIntensitySeeThrough(texture: Identifier): RenderTypeExtension

        fun debugLineStrip(lineWidth: Double): RenderTypeExtension
    }
}