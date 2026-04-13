package net.typho.big_shot_lib.impl

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.Minecraft
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.util.NeoAtlas
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.impl.client.rendering.util.NeoVertexFormatImpl
import net.typho.big_shot_lib.impl.util.getExtensionValue
import org.joml.Vector3f

object InternalUtilImpl : InternalUtil {
    override fun createVertexFormatBuilder(): NeoVertexFormat.Builder {
        return NeoVertexFormatImpl.BuilderImpl()
    }

    override val positionVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        /*get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.POSITION)
        *///? } else {
        get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_POSITION)
        //? }
    override val colorVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        /*get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.COLOR)
        *///? } else {
        get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_COLOR)
        //? }
    override val textureUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        /*get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV0)
        *///? } else {
        get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_UV0)
        //? }
    override val overlayUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        /*get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV1)
        *///? } else {
        get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_UV1)
        //? }
    override val lightUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        /*get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV2)
        *///? } else {
        get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_UV2)
        //? }
    override val normalVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        /*get() = NeoVertexFormatImpl.ElementImpl(VertexFormatElement.NORMAL)
        *///? } else {
        get() = NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_NORMAL)
        //? }

    //? if <1.21.9 {
    override val blitScreenVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.BLIT_SCREEN)
    //? } else {
    /*override val blitScreenVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION)
    *///? }
    override val blockVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.BLOCK)
    override val newEntityVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.NEW_ENTITY)
    override val particleVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.PARTICLE)
    override val positionVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION)
    override val positionColorVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR)
    override val positionColorNormalVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR_NORMAL)
    override val positionColorLightVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR_LIGHTMAP)
    override val positionTexVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX)
    override val positionTexColorVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX_COLOR)
    override val positionColorTexLightVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
    override val positionTexLightColorVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR)
    override val positionTexColorNormalVertexFormat = NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL)

    override fun getTexture(location: NeoIdentifier): GlTexture2D {
        TODO("Not yet implemented")
    }

    override fun getAtlas(location: NeoIdentifier): NeoAtlas {
        //? if <1.21.9 {
        return Minecraft.getInstance().modelManager.getAtlas(location.withPrefix("textures/atlas/").withSuffix(".png").mojang).getExtensionValue()
        //? } else {
        /*return Minecraft.getInstance().atlasManager.getAtlasOrThrow(location.mojang).getExtensionValue()
        *///? }
    }

    override fun transformNormal(
        pose: PoseStack.Pose,
        x: Float,
        y: Float,
        z: Float
    ): IVec3<Float> {
        //? if >=1.20.5 {
        /*return NeoVec3f(pose.transformNormal(x, y, z, Vector3f()))
        *///? } else {
        return NeoVec3f(pose.normal().transform(Vector3f(x, y, z)))
        //? }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getRegistry(key: NeoResourceKey<Registry<T>>): Registry<T>? {
        //? if <1.21.2 {
        return BuiltInRegistries.REGISTRY.get(key.location.mojang) as? Registry<T>
        //? } else {
        /*return BuiltInRegistries.REGISTRY.get(key.location.mojang).map { it as? Registry<T> }.orElse(null)
        *///? }
    }

    override fun mainWindowHandle(): Long {
        //? if <1.21.9 {
        return Minecraft.getInstance().window.window
        //? } else {
        /*return Minecraft.getInstance().window.handle()
        *///? }
    }
}