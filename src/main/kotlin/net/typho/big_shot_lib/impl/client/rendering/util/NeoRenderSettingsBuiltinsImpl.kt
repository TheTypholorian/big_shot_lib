package net.typho.big_shot_lib.impl.client.rendering.util

import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderSettings
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

object NeoRenderSettingsBuiltinsImpl : NeoRenderSettings.Builtins {
    override val solid: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val cutout: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val cutoutMipped: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val translucent: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val translucentMovingBlock: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val leash: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val waterMask: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val armorEntityGlint: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val glintTranslucent: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val glint: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val entityGlint: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val textBackground: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val textBackgroundSeeThrough: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val lightning: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val dragonRays: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val dragonRaysDepth: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val tripwire: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val endPortal: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val endGateway: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val lines: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val lineStrip: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val debugFilledBox: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val debugQuads: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val debugStructureQuads: NeoRenderSettings
        get() = TODO("Not yet implemented")
    override val debugSectionQuads: NeoRenderSettings
        get() = TODO("Not yet implemented")

    override fun armorCutoutNoCull(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entitySolid(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entityCutout(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entityCutoutNoCull(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entityCutoutNoCullZOffset(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun itemEntityTranslucentCull(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entityTranslucent(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entityTranslucentEmissive(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entitySmoothCutout(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun beaconBeam(
        texture: NeoIdentifier,
        affectsOutline: Boolean
    ): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entityDecal(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entityNoOutline(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun entityShadow(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun dragonExplosionAlpha(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun eyes(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun crumbling(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun text(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun textIntensity(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun textPolygonOffset(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun textIntensityPolygonOffset(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun textSeeThrough(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun textIntensitySeeThrough(texture: NeoIdentifier): NeoRenderSettings {
        TODO("Not yet implemented")
    }

    override fun debugLineStrip(lineWidth: Double): NeoRenderSettings {
        TODO("Not yet implemented")
    }
}