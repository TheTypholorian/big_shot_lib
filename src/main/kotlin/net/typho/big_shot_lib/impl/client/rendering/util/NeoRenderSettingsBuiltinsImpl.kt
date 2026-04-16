package net.typho.big_shot_lib.impl.client.rendering.util

import net.minecraft.client.renderer.RenderType
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderSettings
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.impl.mojang

object NeoRenderSettingsBuiltinsImpl : NeoRenderSettings.Builtins {
    override val solid: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.solid())
    override val cutout: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.cutout())
    override val cutoutMipped: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.cutoutMipped())
    override val translucent: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.translucent())
    override val translucentMovingBlock: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.translucentMovingBlock())
    override val leash: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.leash())
    override val waterMask: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.waterMask())
    override val armorEntityGlint: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.armorEntityGlint())
    override val glintTranslucent: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.glintTranslucent())
    override val glint: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.glint())
    override val entityGlint: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.entityGlint())
    override val textBackground: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.textBackground())
    override val textBackgroundSeeThrough: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.textBackgroundSeeThrough())
    override val lightning: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.lightning())
    override val tripwire: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.tripwire())
    override val endPortal: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.endPortal())
    override val endGateway: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.endGateway())
    override val lines: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.lines())
    override val lineStrip: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.lineStrip())
    override val debugFilledBox: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.debugFilledBox())
    override val debugQuads: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.debugQuads())
    override val debugSectionQuads: NeoRenderSettings = NeoRenderSettingsImpl(RenderType.debugSectionQuads())

    override fun armorCutoutNoCull(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.armorCutoutNoCull(texture.mojang))
    }

    override fun entitySolid(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entitySolid(texture.mojang))
    }

    override fun entityCutout(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entityCutout(texture.mojang))
    }

    override fun entityCutoutNoCull(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entityCutoutNoCull(texture.mojang, affectsOutline))
    }

    override fun entityCutoutNoCullZOffset(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entityCutoutNoCullZOffset(texture.mojang, affectsOutline))
    }

    override fun itemEntityTranslucentCull(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.itemEntityTranslucentCull(texture.mojang))
    }

    override fun entityTranslucent(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entityTranslucent(texture.mojang, affectsOutline))
    }

    override fun entityTranslucentEmissive(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entityTranslucentEmissive(texture.mojang, affectsOutline))
    }

    override fun entitySmoothCutout(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entitySmoothCutout(texture.mojang))
    }

    override fun beaconBeam(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.beaconBeam(texture.mojang, affectsOutline))
    }

    override fun entityDecal(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entityDecal(texture.mojang))
    }

    override fun entityNoOutline(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entityNoOutline(texture.mojang))
    }

    override fun entityShadow(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.entityShadow(texture.mojang))
    }

    override fun dragonExplosionAlpha(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.dragonExplosionAlpha(texture.mojang))
    }

    override fun eyes(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.eyes(texture.mojang))
    }

    override fun crumbling(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.crumbling(texture.mojang))
    }

    override fun text(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.text(texture.mojang))
    }

    override fun textIntensity(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.textIntensity(texture.mojang))
    }

    override fun textPolygonOffset(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.textPolygonOffset(texture.mojang))
    }

    override fun textIntensityPolygonOffset(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.textIntensityPolygonOffset(texture.mojang))
    }

    override fun textSeeThrough(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.textSeeThrough(texture.mojang))
    }

    override fun textIntensitySeeThrough(texture: NeoIdentifier): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.textIntensitySeeThrough(texture.mojang))
    }

    override fun debugLineStrip(lineWidth: Double): NeoRenderSettings {
        return NeoRenderSettingsImpl(RenderType.debugLineStrip(lineWidth))
    }
}