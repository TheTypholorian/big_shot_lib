package net.typho.big_shot_lib.impl.client.rendering.util

import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType

object NeoRenderTypeBuiltinsImpl : NeoRenderType.Builtins {
    override val solid: NeoRenderType = RenderType.solid()
    override val cutout: NeoRenderType = RenderType.cutout()
    override val cutoutMipped: NeoRenderType = RenderType.cutoutMipped()
    override val translucent: NeoRenderType = RenderType.translucent()
    override val translucentMovingBlock: NeoRenderType = RenderType.translucentMovingBlock()
    override val leash: NeoRenderType = RenderType.leash()
    override val waterMask: NeoRenderType = RenderType.waterMask()
    override val armorEntityGlint: NeoRenderType = RenderType.armorEntityGlint()
    override val glintTranslucent: NeoRenderType = RenderType.glintTranslucent()
    override val glint: NeoRenderType = RenderType.glint()
    override val entityGlint: NeoRenderType = RenderType.entityGlint()
    override val textBackground: NeoRenderType = RenderType.textBackground()
    override val textBackgroundSeeThrough: NeoRenderType = RenderType.textBackgroundSeeThrough()
    override val lightning: NeoRenderType = RenderType.lightning()
    override val tripwire: NeoRenderType = RenderType.tripwire()
    override val endPortal: NeoRenderType = RenderType.endPortal()
    override val endGateway: NeoRenderType = RenderType.endGateway()
    override val lines: NeoRenderType = RenderType.lines()
    override val lineStrip: NeoRenderType = RenderType.lineStrip()
    override val debugFilledBox: NeoRenderType = RenderType.debugFilledBox()
    override val debugQuads: NeoRenderType = RenderType.debugQuads()
    override val debugSectionQuads: NeoRenderType = RenderType.debugSectionQuads()

    override fun armorCutoutNoCull(texture: Identifier): NeoRenderType {
        return RenderType.armorCutoutNoCull(texture)
    }

    override fun entitySolid(texture: Identifier): NeoRenderType {
        return RenderType.entitySolid(texture)
    }

    override fun entityCutout(texture: Identifier): NeoRenderType {
        return RenderType.entityCutout(texture)
    }

    override fun entityCutoutNoCull(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityCutoutNoCull(texture, affectsOutline)
    }

    override fun entityCutoutNoCullZOffset(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityCutoutNoCullZOffset(texture, affectsOutline)
    }

    override fun itemEntityTranslucentCull(texture: Identifier): NeoRenderType {
        return RenderType.itemEntityTranslucentCull(texture)
    }

    override fun entityTranslucent(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityTranslucent(texture, affectsOutline)
    }

    override fun entityTranslucentEmissive(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityTranslucentEmissive(texture, affectsOutline)
    }

    override fun entitySmoothCutout(texture: Identifier): NeoRenderType {
        return RenderType.entitySmoothCutout(texture)
    }

    override fun beaconBeam(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.beaconBeam(texture, affectsOutline)
    }

    override fun entityDecal(texture: Identifier): NeoRenderType {
        return RenderType.entityDecal(texture)
    }

    override fun entityNoOutline(texture: Identifier): NeoRenderType {
        return RenderType.entityNoOutline(texture)
    }

    override fun entityShadow(texture: Identifier): NeoRenderType {
        return RenderType.entityShadow(texture)
    }

    override fun dragonExplosionAlpha(texture: Identifier): NeoRenderType {
        return RenderType.dragonExplosionAlpha(texture)
    }

    override fun eyes(texture: Identifier): NeoRenderType {
        return RenderType.eyes(texture)
    }

    override fun crumbling(texture: Identifier): NeoRenderType {
        return RenderType.crumbling(texture)
    }

    override fun text(texture: Identifier): NeoRenderType {
        return RenderType.text(texture)
    }

    override fun textIntensity(texture: Identifier): NeoRenderType {
        return RenderType.textIntensity(texture)
    }

    override fun textPolygonOffset(texture: Identifier): NeoRenderType {
        return RenderType.textPolygonOffset(texture)
    }

    override fun textIntensityPolygonOffset(texture: Identifier): NeoRenderType {
        return RenderType.textIntensityPolygonOffset(texture)
    }

    override fun textSeeThrough(texture: Identifier): NeoRenderType {
        return RenderType.textSeeThrough(texture)
    }

    override fun textIntensitySeeThrough(texture: Identifier): NeoRenderType {
        return RenderType.textIntensitySeeThrough(texture)
    }

    override fun debugLineStrip(lineWidth: Double): NeoRenderType {
        return RenderType.debugLineStrip(lineWidth)
    }
}