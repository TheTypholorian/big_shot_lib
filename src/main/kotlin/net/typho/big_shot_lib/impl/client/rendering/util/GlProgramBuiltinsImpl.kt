package net.typho.big_shot_lib.impl.client.rendering.util

import net.minecraft.client.renderer.GameRenderer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.impl.util.getExtensionValue

object GlProgramBuiltinsImpl : GlProgram.Builtins {
    override val position: GlProgram?
        get() = GameRenderer.getPositionShader()?.getExtensionValue()
    override val positionColor: GlProgram?
        get() = GameRenderer.getPositionColorShader()?.getExtensionValue()
    override val positonTex: GlProgram?
        get() = GameRenderer.getPositionTexShader()?.getExtensionValue()
    override val positonTexColor: GlProgram?
        get() = GameRenderer.getPositionTexColorShader()?.getExtensionValue()
    override val particle: GlProgram?
        get() = GameRenderer.getParticleShader()?.getExtensionValue()
    override val positionColorLightmap: GlProgram?
        get() = GameRenderer.getPositionColorLightmapShader()?.getExtensionValue()
    override val positionColorTexLightmap: GlProgram?
        get() = GameRenderer.getPositionColorTexLightmapShader()?.getExtensionValue()
    override val solid: GlProgram?
        get() = GameRenderer.getRendertypeSolidShader()?.getExtensionValue()
    override val cutoutMipped: GlProgram?
        get() = GameRenderer.getRendertypeCutoutMippedShader()?.getExtensionValue()
    override val cutout: GlProgram?
        get() = GameRenderer.getRendertypeCutoutShader()?.getExtensionValue()
    override val translucent: GlProgram?
        get() = GameRenderer.getRendertypeTranslucentShader()?.getExtensionValue()
    override val translucentMovingBlock: GlProgram?
        get() = GameRenderer.getRendertypeTranslucentMovingBlockShader()?.getExtensionValue()
    override val armorCutoutNoCull: GlProgram?
        get() = GameRenderer.getRendertypeArmorCutoutNoCullShader()?.getExtensionValue()
    override val entitySolid: GlProgram?
        get() = GameRenderer.getRendertypeEntitySolidShader()?.getExtensionValue()
    override val entityCutout: GlProgram?
        get() = GameRenderer.getRendertypeEntityCutoutShader()?.getExtensionValue()
    override val entityCutoutNoCull: GlProgram?
        get() = GameRenderer.getRendertypeEntityCutoutNoCullShader()?.getExtensionValue()
    override val entityCutoutNoCullZOffset: GlProgram?
        get() = GameRenderer.getRendertypeEntityCutoutNoCullZOffsetShader()?.getExtensionValue()
    override val itemEntityTranslucentCull: GlProgram?
        get() = GameRenderer.getRendertypeItemEntityTranslucentCullShader()?.getExtensionValue()
    override val entityTranslucentCull: GlProgram?
        get() = GameRenderer.getRendertypeEntityTranslucentCullShader()?.getExtensionValue()
    override val entityTranslucent: GlProgram?
        get() = GameRenderer.getRendertypeEntityTranslucentShader()?.getExtensionValue()
    override val entityTranslucentEmissive: GlProgram?
        get() = GameRenderer.getRendertypeEntityTranslucentEmissiveShader()?.getExtensionValue()
    override val entitySmoothCutout: GlProgram?
        get() = GameRenderer.getRendertypeEntitySmoothCutoutShader()?.getExtensionValue()
    override val beaconBeam: GlProgram?
        get() = GameRenderer.getRendertypeBeaconBeamShader()?.getExtensionValue()
    override val entityDecal: GlProgram?
        get() = GameRenderer.getRendertypeEntityDecalShader()?.getExtensionValue()
    override val entityNoOutline: GlProgram?
        get() = GameRenderer.getRendertypeEntityNoOutlineShader()?.getExtensionValue()
    override val entityShadow: GlProgram?
        get() = GameRenderer.getRendertypeEntityShadowShader()?.getExtensionValue()
    override val entityAlpha: GlProgram?
        get() = GameRenderer.getRendertypeEntityAlphaShader()?.getExtensionValue()
    override val eyes: GlProgram?
        get() = GameRenderer.getRendertypeEyesShader()?.getExtensionValue()
    override val energySwirl: GlProgram?
        get() = GameRenderer.getRendertypeEnergySwirlShader()?.getExtensionValue()
    override val breezeWind: GlProgram?
        get() = GameRenderer.getRendertypeBreezeWindShader()?.getExtensionValue()
    override val leash: GlProgram?
        get() = GameRenderer.getRendertypeLeashShader()?.getExtensionValue()
    override val waterMask: GlProgram?
        get() = GameRenderer.getRendertypeWaterMaskShader()?.getExtensionValue()
    override val outline: GlProgram?
        get() = GameRenderer.getRendertypeOutlineShader()?.getExtensionValue()
    override val armorGlint: GlProgram?
        get() = GameRenderer.getRendertypeArmorGlintShader()?.getExtensionValue()
    override val armorEntityGlint: GlProgram?
        get() = GameRenderer.getRendertypeArmorEntityGlintShader()?.getExtensionValue()
    override val glintTranslucent: GlProgram?
        get() = GameRenderer.getRendertypeGlintTranslucentShader()?.getExtensionValue()
    override val glint: GlProgram?
        get() = GameRenderer.getRendertypeGlintShader()?.getExtensionValue()
    override val glintDirect: GlProgram?
        get() = GameRenderer.getRendertypeGlintDirectShader()?.getExtensionValue()
    override val entityGlint: GlProgram?
        get() = GameRenderer.getRendertypeEntityGlintShader()?.getExtensionValue()
    override val entityGlintDirect: GlProgram?
        get() = GameRenderer.getRendertypeEntityGlintDirectShader()?.getExtensionValue()
    override val text: GlProgram?
        get() = GameRenderer.getRendertypeTextShader()?.getExtensionValue()
    override val textBackground: GlProgram?
        get() = GameRenderer.getRendertypeTextBackgroundShader()?.getExtensionValue()
    override val textIntensity: GlProgram?
        get() = GameRenderer.getRendertypeTextIntensityShader()?.getExtensionValue()
    override val textSeeThrough: GlProgram?
        get() = GameRenderer.getRendertypeTextSeeThroughShader()?.getExtensionValue()
    override val textBackgroundSeeThrough: GlProgram?
        get() = GameRenderer.getRendertypeTextBackgroundSeeThroughShader()?.getExtensionValue()
    override val textIntensitySeeThrough: GlProgram?
        get() = GameRenderer.getRendertypeTextIntensitySeeThroughShader()?.getExtensionValue()
    override val lightning: GlProgram?
        get() = GameRenderer.getRendertypeLightningShader()?.getExtensionValue()
    override val tripwire: GlProgram?
        get() = GameRenderer.getRendertypeTripwireShader()?.getExtensionValue()
    override val endPortal: GlProgram?
        get() = GameRenderer.getRendertypeEndPortalShader()?.getExtensionValue()
    override val endGateway: GlProgram?
        get() = GameRenderer.getRendertypeEndGatewayShader()?.getExtensionValue()
    override val clouds: GlProgram?
        get() = GameRenderer.getRendertypeCloudsShader()?.getExtensionValue()
    override val lines: GlProgram?
        get() = GameRenderer.getRendertypeLinesShader()?.getExtensionValue()
    override val crumbling: GlProgram?
        get() = GameRenderer.getRendertypeCrumblingShader()?.getExtensionValue()
    override val gui: GlProgram?
        get() = GameRenderer.getRendertypeGuiShader()?.getExtensionValue()
    override val guiOverlay: GlProgram?
        get() = GameRenderer.getRendertypeGuiOverlayShader()?.getExtensionValue()
    override val guiTextHighlight: GlProgram?
        get() = GameRenderer.getRendertypeGuiTextHighlightShader()?.getExtensionValue()
    override val guiGhostRecipeOverlay: GlProgram?
        get() = GameRenderer.getRendertypeGuiGhostRecipeOverlayShader()?.getExtensionValue()
}