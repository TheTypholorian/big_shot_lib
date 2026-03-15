package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.*
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.opengl.GL11.glTexImage1D
import org.lwjgl.opengl.GL11.glTexImage2D
import org.lwjgl.opengl.GL12.glTexImage3D
import org.lwjgl.opengl.GL32.glTexImage2DMultisample
import org.lwjgl.opengl.GL32.glTexImage3DMultisample
import org.lwjgl.opengl.GL42.*
import org.lwjgl.opengl.GL43.glTexStorage2DMultisample
import org.lwjgl.opengl.GL43.glTexStorage3DMultisample

interface GlBoundTexture : GlBoundResource<GlTexture> {
    val target: GlTextureTarget
    var borderColor: NeoColor

    var compareMode: GlTextureCompareMode
    var compareFunc: GlAlphaFunction

    var baseLevel: Int
    var maxLevel: Int

    var minLod: Int
    var maxLod: Int
    var lodBias: Int

    var minFilter: GlTextureMinFilter
    var magFilter: GlTextureMagFilter

    var wrapS: GlTextureWrapMode
    var wrapT: GlTextureWrapMode
    var wrapR: GlTextureWrapMode

    var sizzleR: GlTextureSwizzle
    var sizzleG: GlTextureSwizzle
    var sizzleB: GlTextureSwizzle
    var sizzleA: GlTextureSwizzle
    var sizzleRGBA: GlTextureSwizzle

    fun textureDataMutable1D(
        width: Int,
        format: GlTextureFormat,
        level: Int = 0,
        border: Int = 0,
        data: NeoBuffer = NeoBuffer.NULL
    )

    fun textureDataMutable2D(
        width: Int,
        height: Int,
        format: GlTextureFormat,
        level: Int = 0,
        border: Int = 0,
        data: NeoBuffer = NeoBuffer.NULL
    )

    fun textureDataMutable2DMultisample(
        width: Int,
        height: Int,
        format: GlTextureFormat,
        samples: Int,
        fixedSampleLocations: Boolean
    )

    fun textureDataMutable3D(
        width: Int,
        height: Int,
        depth: Int,
        format: GlTextureFormat,
        level: Int = 0,
        border: Int = 0,
        data: NeoBuffer = NeoBuffer.NULL
    )

    fun textureDataMutable3DMultisample(
        width: Int,
        height: Int,
        depth: Int,
        format: GlTextureFormat,
        samples: Int,
        fixedSampleLocations: Boolean
    )

    fun textureDataImmutable1D(
        width: Int,
        format: GlTextureFormat,
        levels: Int = 1
    )

    fun textureDataImmutable2D(
        width: Int,
        height: Int,
        format: GlTextureFormat,
        levels: Int = 1
    )

    fun textureDataImmutable2DMultisample(
        width: Int,
        height: Int,
        format: GlTextureFormat,
        samples: Int,
        fixedSampleLocations: Boolean
    )

    fun textureDataImmutable3D(
        width: Int,
        height: Int,
        depth: Int,
        format: GlTextureFormat,
        levels: Int = 1
    )

    fun textureDataImmutable3DMultisample(
        width: Int,
        height: Int,
        depth: Int,
        format: GlTextureFormat,
        samples: Int,
        fixedSampleLocations: Boolean
    )

    open class Basic(
        override val resource: GlTexture,
        override val target: GlTextureTarget,
        override val handle: GlStateStack.Handle<Int>
    ) : GlBoundTexture {
        override var borderColor: NeoColor
            get() = NeoColor.RGBA(glGetTexParameteri(target.glId, GL_TEXTURE_BORDER_COLOR))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_BORDER_COLOR, value.toRGBA())
        override var compareMode: GlTextureCompareMode
            get() = GlNamed.getEnum<GlTextureCompareMode>(glGetTexParameteri(target.glId, GL_TEXTURE_COMPARE_MODE))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_COMPARE_MODE, value.glId)
        override var compareFunc: GlAlphaFunction
            get() = GlNamed.getEnum<GlAlphaFunction>(glGetTexParameteri(target.glId, GL_TEXTURE_COMPARE_FUNC))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_COMPARE_FUNC, value.glId)
        override var baseLevel: Int
            get() = glGetTexParameteri(target.glId, GL_TEXTURE_BASE_LEVEL)
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_BASE_LEVEL, value)
        override var maxLevel: Int
            get() = glGetTexParameteri(target.glId, GL_TEXTURE_MAX_LEVEL)
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_MAX_LEVEL, value)
        override var minLod: Int
            get() = glGetTexParameteri(target.glId, GL_TEXTURE_MIN_LOD)
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_MIN_LOD, value)
        override var maxLod: Int
            get() = glGetTexParameteri(target.glId, GL_TEXTURE_MAX_LOD)
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_MAX_LOD, value)
        override var lodBias: Int
            get() = glGetTexParameteri(target.glId, GL_TEXTURE_LOD_BIAS)
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_LOD_BIAS, value)
        override var minFilter: GlTextureMinFilter
            get() = GlNamed.getEnum<GlTextureMinFilter>(glGetTexParameteri(target.glId, GL_TEXTURE_MIN_FILTER))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_MIN_FILTER, value.glId)
        override var magFilter: GlTextureMagFilter
            get() = GlNamed.getEnum<GlTextureMagFilter>(glGetTexParameteri(target.glId, GL_TEXTURE_MAG_FILTER))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_MAG_FILTER, value.glId)
        override var wrapS: GlTextureWrapMode
            get() = GlNamed.getEnum<GlTextureWrapMode>(glGetTexParameteri(target.glId, GL_TEXTURE_WRAP_S))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_WRAP_S, value.glId)
        override var wrapT: GlTextureWrapMode
            get() = GlNamed.getEnum<GlTextureWrapMode>(glGetTexParameteri(target.glId, GL_TEXTURE_WRAP_T))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_WRAP_T, value.glId)
        override var wrapR: GlTextureWrapMode
            get() = GlNamed.getEnum<GlTextureWrapMode>(glGetTexParameteri(target.glId, GL_TEXTURE_WRAP_R))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_WRAP_R, value.glId)
        override var sizzleR: GlTextureSwizzle
            get() = GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_R))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_R, value.glId)
        override var sizzleG: GlTextureSwizzle
            get() = GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_G))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_G, value.glId)
        override var sizzleB: GlTextureSwizzle
            get() = GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_B))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_B, value.glId)
        override var sizzleA: GlTextureSwizzle
            get() = GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_A))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_A, value.glId)
        override var sizzleRGBA: GlTextureSwizzle
            get() = GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_RGBA))
            set(value) = glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_RGBA, value.glId)

        override fun textureDataMutable1D(
            width: Int,
            format: GlTextureFormat,
            level: Int,
            border: Int,
            data: NeoBuffer
        ) {
            glTexImage1D(
                target.glId,
                level,
                format.internalId,
                width,
                border,
                format.glId,
                format.type,
                data.address
            )
        }

        override fun textureDataMutable2D(
            width: Int,
            height: Int,
            format: GlTextureFormat,
            level: Int,
            border: Int,
            data: NeoBuffer
        ) {
            glTexImage2D(
                target.glId,
                level,
                format.internalId,
                width,
                height,
                border,
                format.glId,
                format.type,
                data.address
            )
        }

        override fun textureDataMutable2DMultisample(
            width: Int,
            height: Int,
            format: GlTextureFormat,
            samples: Int,
            fixedSampleLocations: Boolean
        ) {
            glTexImage2DMultisample(
                target.glId,
                samples,
                format.internalId,
                width,
                height,
                fixedSampleLocations
            )
        }

        override fun textureDataMutable3D(
            width: Int,
            height: Int,
            depth: Int,
            format: GlTextureFormat,
            level: Int,
            border: Int,
            data: NeoBuffer
        ) {
            glTexImage3D(
                target.glId,
                level,
                format.internalId,
                width,
                height,
                depth,
                border,
                format.glId,
                format.type,
                data.address
            )
        }

        override fun textureDataMutable3DMultisample(
            width: Int,
            height: Int,
            depth: Int,
            format: GlTextureFormat,
            samples: Int,
            fixedSampleLocations: Boolean
        ) {
            glTexImage3DMultisample(
                target.glId,
                samples,
                format.internalId,
                width,
                height,
                depth,
                fixedSampleLocations
            )
        }

        override fun textureDataImmutable1D(
            width: Int,
            format: GlTextureFormat,
            levels: Int
        ) {
            glTexStorage1D(target.glId, levels, format.internalId, width)
        }

        override fun textureDataImmutable2D(
            width: Int,
            height: Int,
            format: GlTextureFormat,
            levels: Int
        ) {
            glTexStorage2D(target.glId, levels, format.internalId, width, height)
        }

        override fun textureDataImmutable2DMultisample(
            width: Int,
            height: Int,
            format: GlTextureFormat,
            samples: Int,
            fixedSampleLocations: Boolean
        ) {
            glTexStorage2DMultisample(target.glId, samples, format.internalId, width, height, fixedSampleLocations)
        }

        override fun textureDataImmutable3D(
            width: Int,
            height: Int,
            depth: Int,
            format: GlTextureFormat,
            levels: Int
        ) {
            glTexStorage3D(target.glId, levels, format.internalId, width, height, depth)
        }

        override fun textureDataImmutable3DMultisample(
            width: Int,
            height: Int,
            depth: Int,
            format: GlTextureFormat,
            samples: Int,
            fixedSampleLocations: Boolean
        ) {
            glTexStorage3DMultisample(target.glId, samples, format.internalId, width, height, depth, fixedSampleLocations)
        }
    }
}