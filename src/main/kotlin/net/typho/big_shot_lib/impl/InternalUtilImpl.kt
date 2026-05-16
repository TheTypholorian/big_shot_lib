package net.typho.big_shot_lib.impl

import com.mojang.blaze3d.shaders.Program
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.util.NeoAtlas
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.impl.client.rendering.opengl.ShaderInstanceExtension
import net.typho.big_shot_lib.impl.util.getExtensionValue
import net.typho.big_shot_lib.impl.util.setExtensionValue
import org.joml.Vector3f
import sun.misc.Unsafe
import java.lang.reflect.Modifier

object InternalUtilImpl : InternalUtil {
    @JvmField
    val UNSAFE = getUnsafe()

    private fun getUnsafe(): Unsafe {
        val fields = Unsafe::class.java.getDeclaredFields()

        for (field in fields) {
            if (field.type != Unsafe::class.java) {
                continue
            }

            val modifiers = field.modifiers
            if (!(Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers))) {
                continue
            }

            try {
                field.setAccessible(true)
                return field.get(null) as Unsafe
            } catch (ignored: Exception) {
            }
        }

        throw UnsupportedOperationException("Big Shot Lib requires sun.misc.Unsafe to be available.")
    }

    override fun createVertexFormatBuilder(): NeoVertexFormat.Builder {
        return VertexFormat.builder().getExtensionValue()
    }

    override val positionVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.POSITION.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_POSITION.getExtensionValue()
        *///? }
    override val colorVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.COLOR.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_COLOR.getExtensionValue()
        *///? }
    override val textureUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.UV0.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_UV0.getExtensionValue()
        *///? }
    override val overlayUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.UV1.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_UV1.getExtensionValue()
        *///? }
    override val lightUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.UV2.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_UV2.getExtensionValue()
        *///? }
    override val normalVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.NORMAL.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_NORMAL.getExtensionValue()
        *///? }

    //? if <1.21.9 {
    override val blitScreenVertexFormat: NeoVertexFormat = DefaultVertexFormat.BLIT_SCREEN.getExtensionValue()
    //? } else {
    /*override val blitScreenVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION.getExtensionValue()
    *///? }
    override val blockVertexFormat: NeoVertexFormat = DefaultVertexFormat.BLOCK.getExtensionValue()
    override val newEntityVertexFormat: NeoVertexFormat = DefaultVertexFormat.NEW_ENTITY.getExtensionValue()
    override val particleVertexFormat: NeoVertexFormat = DefaultVertexFormat.PARTICLE.getExtensionValue()
    override val positionVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION.getExtensionValue()
    override val positionColorVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_COLOR.getExtensionValue()
    override val positionColorNormalVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_COLOR_NORMAL.getExtensionValue()
    override val positionColorLightVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_COLOR_LIGHTMAP.getExtensionValue()
    override val positionTexVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_TEX.getExtensionValue()
    override val positionTexColorVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_TEX_COLOR.getExtensionValue()
    override val positionColorTexLightVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP.getExtensionValue()
    override val positionTexLightColorVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR.getExtensionValue()
    override val positionTexColorNormalVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.getExtensionValue()

    override fun getTexture(location: NeoIdentifier): GlTexture2D {
        return Minecraft.getInstance().textureManager.getTexture(location.mojang).getExtensionValue()
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
        return NeoVec3f(pose.transformNormal(x, y, z, Vector3f()))
        //? } else {
        /*return NeoVec3f(pose.normal().transform(Vector3f(x, y, z)))
        *///? }
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

    override fun createShader(location: NeoIdentifier, type: GlShaderType, glId: Int): GlShader {
        return when (type) {
            GlShaderType.VERTEX -> Program(Program.Type.VERTEX, glId, location.toShortString()).getExtensionValue()
            GlShaderType.FRAGMENT -> Program(Program.Type.VERTEX, glId, location.toShortString()).getExtensionValue()
            else -> NeoGlShader(location, type, glId)
        }
    }

    override fun createProgram(
        location: NeoIdentifier,
        format: NeoVertexFormat,
        glId: Int
    ): GlProgram {
        val shader = UNSAFE.allocateInstance(ShaderInstance::class.java) as ShaderInstance
        (shader as ShaderInstanceExtension).`big_shot_lib$init`(location, format, glId)
        return shader.getExtensionValue<GlProgram>()
    }
}