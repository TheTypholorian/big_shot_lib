package net.typho.big_shot_lib.impl.client.rendering.util

import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.impl.util.getExtensionValue

object NeoRenderTypeBuiltinsImpl : NeoRenderType.Builtins {
    override val solid: NeoRenderType = RenderType.solid().getExtensionValue()
    override val cutout: NeoRenderType = RenderType.cutout().getExtensionValue()
    override val cutoutMipped: NeoRenderType = RenderType.cutoutMipped().getExtensionValue()
    override val translucent: NeoRenderType = RenderType.translucent().getExtensionValue()
    override val translucentMovingBlock: NeoRenderType = RenderType.translucentMovingBlock().getExtensionValue()
    override val leash: NeoRenderType = RenderType.leash().getExtensionValue()
    override val waterMask: NeoRenderType = RenderType.waterMask().getExtensionValue()
    override val armorEntityGlint: NeoRenderType = RenderType.armorEntityGlint().getExtensionValue()
    override val glintTranslucent: NeoRenderType = RenderType.glintTranslucent().getExtensionValue()
    override val glint: NeoRenderType = RenderType.glint().getExtensionValue()
    override val entityGlint: NeoRenderType = RenderType.entityGlint().getExtensionValue()
    override val textBackground: NeoRenderType = RenderType.textBackground().getExtensionValue()
    override val textBackgroundSeeThrough: NeoRenderType = RenderType.textBackgroundSeeThrough().getExtensionValue()
    override val lightning: NeoRenderType = RenderType.lightning().getExtensionValue()
    override val tripwire: NeoRenderType = RenderType.tripwire().getExtensionValue()
    override val endPortal: NeoRenderType = RenderType.endPortal().getExtensionValue()
    override val endGateway: NeoRenderType = RenderType.endGateway().getExtensionValue()
    override val lines: NeoRenderType = RenderType.lines().getExtensionValue()
    override val lineStrip: NeoRenderType = RenderType.lineStrip().getExtensionValue()
    override val debugFilledBox: NeoRenderType = RenderType.debugFilledBox().getExtensionValue()
    override val debugQuads: NeoRenderType = RenderType.debugQuads().getExtensionValue()
    override val debugSectionQuads: NeoRenderType = RenderType.debugSectionQuads().getExtensionValue()

    override fun armorCutoutNoCull(texture: Identifier): NeoRenderType {
        return RenderType.armorCutoutNoCull(texture).getExtensionValue()
    }

    override fun entitySolid(texture: Identifier): NeoRenderType {
        return RenderType.entitySolid(texture).getExtensionValue()
    }

    override fun entityCutout(texture: Identifier): NeoRenderType {
        return RenderType.entityCutout(texture).getExtensionValue()
    }

    override fun entityCutoutNoCull(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityCutoutNoCull(texture, affectsOutline).getExtensionValue()
    }

    override fun entityCutoutNoCullZOffset(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityCutoutNoCullZOffset(texture, affectsOutline).getExtensionValue()
    }

    override fun itemEntityTranslucentCull(texture: Identifier): NeoRenderType {
        return RenderType.itemEntityTranslucentCull(texture).getExtensionValue()
    }

    override fun entityTranslucent(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityTranslucent(texture, affectsOutline).getExtensionValue()
    }

    override fun entityTranslucentEmissive(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityTranslucentEmissive(texture, affectsOutline).getExtensionValue()
    }

    override fun entitySmoothCutout(texture: Identifier): NeoRenderType {
        return RenderType.entitySmoothCutout(texture).getExtensionValue()
    }

    override fun beaconBeam(
        texture: Identifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.beaconBeam(texture, affectsOutline).getExtensionValue()
    }

    override fun entityDecal(texture: Identifier): NeoRenderType {
        return RenderType.entityDecal(texture).getExtensionValue()
    }

    override fun entityNoOutline(texture: Identifier): NeoRenderType {
        return RenderType.entityNoOutline(texture).getExtensionValue()
    }

    override fun entityShadow(texture: Identifier): NeoRenderType {
        return RenderType.entityShadow(texture).getExtensionValue()
    }

    override fun dragonExplosionAlpha(texture: Identifier): NeoRenderType {
        return RenderType.dragonExplosionAlpha(texture).getExtensionValue()
    }

    override fun eyes(texture: Identifier): NeoRenderType {
        return RenderType.eyes(texture).getExtensionValue()
    }

    override fun crumbling(texture: Identifier): NeoRenderType {
        return RenderType.crumbling(texture).getExtensionValue()
    }

    override fun text(texture: Identifier): NeoRenderType {
        return RenderType.text(texture).getExtensionValue()
    }

    override fun textIntensity(texture: Identifier): NeoRenderType {
        return RenderType.textIntensity(texture).getExtensionValue()
    }

    override fun textPolygonOffset(texture: Identifier): NeoRenderType {
        return RenderType.textPolygonOffset(texture).getExtensionValue()
    }

    override fun textIntensityPolygonOffset(texture: Identifier): NeoRenderType {
        return RenderType.textIntensityPolygonOffset(texture).getExtensionValue()
    }

    override fun textSeeThrough(texture: Identifier): NeoRenderType {
        return RenderType.textSeeThrough(texture).getExtensionValue()
    }

    override fun textIntensitySeeThrough(texture: Identifier): NeoRenderType {
        return RenderType.textIntensitySeeThrough(texture).getExtensionValue()
    }

    override fun debugLineStrip(lineWidth: Double): NeoRenderType {
        return RenderType.debugLineStrip(lineWidth).getExtensionValue()
    }
}