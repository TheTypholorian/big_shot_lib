package net.typho.big_shot_lib.impl.client.rendering.util

import net.minecraft.client.renderer.RenderType
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.impl.mojang
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

    override fun armorCutoutNoCull(texture: NeoIdentifier): NeoRenderType {
        return RenderType.armorCutoutNoCull(texture.mojang).getExtensionValue()
    }

    override fun entitySolid(texture: NeoIdentifier): NeoRenderType {
        return RenderType.entitySolid(texture.mojang).getExtensionValue()
    }

    override fun entityCutout(texture: NeoIdentifier): NeoRenderType {
        return RenderType.entityCutout(texture.mojang).getExtensionValue()
    }

    override fun entityCutoutNoCull(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityCutoutNoCull(texture.mojang, affectsOutline).getExtensionValue()
    }

    override fun entityCutoutNoCullZOffset(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityCutoutNoCullZOffset(texture.mojang, affectsOutline).getExtensionValue()
    }

    override fun itemEntityTranslucentCull(texture: NeoIdentifier): NeoRenderType {
        return RenderType.itemEntityTranslucentCull(texture.mojang).getExtensionValue()
    }

    override fun entityTranslucent(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityTranslucent(texture.mojang, affectsOutline).getExtensionValue()
    }

    override fun entityTranslucentEmissive(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.entityTranslucentEmissive(texture.mojang, affectsOutline).getExtensionValue()
    }

    override fun entitySmoothCutout(texture: NeoIdentifier): NeoRenderType {
        return RenderType.entitySmoothCutout(texture.mojang).getExtensionValue()
    }

    override fun beaconBeam(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderType {
        return RenderType.beaconBeam(texture.mojang, affectsOutline).getExtensionValue()
    }

    override fun entityDecal(texture: NeoIdentifier): NeoRenderType {
        return RenderType.entityDecal(texture.mojang).getExtensionValue()
    }

    override fun entityNoOutline(texture: NeoIdentifier): NeoRenderType {
        return RenderType.entityNoOutline(texture.mojang).getExtensionValue()
    }

    override fun entityShadow(texture: NeoIdentifier): NeoRenderType {
        return RenderType.entityShadow(texture.mojang).getExtensionValue()
    }

    override fun dragonExplosionAlpha(texture: NeoIdentifier): NeoRenderType {
        return RenderType.dragonExplosionAlpha(texture.mojang).getExtensionValue()
    }

    override fun eyes(texture: NeoIdentifier): NeoRenderType {
        return RenderType.eyes(texture.mojang).getExtensionValue()
    }

    override fun crumbling(texture: NeoIdentifier): NeoRenderType {
        return RenderType.crumbling(texture.mojang).getExtensionValue()
    }

    override fun text(texture: NeoIdentifier): NeoRenderType {
        return RenderType.text(texture.mojang).getExtensionValue()
    }

    override fun textIntensity(texture: NeoIdentifier): NeoRenderType {
        return RenderType.textIntensity(texture.mojang).getExtensionValue()
    }

    override fun textPolygonOffset(texture: NeoIdentifier): NeoRenderType {
        return RenderType.textPolygonOffset(texture.mojang).getExtensionValue()
    }

    override fun textIntensityPolygonOffset(texture: NeoIdentifier): NeoRenderType {
        return RenderType.textIntensityPolygonOffset(texture.mojang).getExtensionValue()
    }

    override fun textSeeThrough(texture: NeoIdentifier): NeoRenderType {
        return RenderType.textSeeThrough(texture.mojang).getExtensionValue()
    }

    override fun textIntensitySeeThrough(texture: NeoIdentifier): NeoRenderType {
        return RenderType.textIntensitySeeThrough(texture.mojang).getExtensionValue()
    }

    override fun debugLineStrip(lineWidth: Double): NeoRenderType {
        return RenderType.debugLineStrip(lineWidth).getExtensionValue()
    }
}