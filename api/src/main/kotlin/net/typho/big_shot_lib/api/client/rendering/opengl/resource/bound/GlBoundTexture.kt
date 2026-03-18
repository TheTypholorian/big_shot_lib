package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.*
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
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
        data: NeoBuffer? = null
    )

    fun textureDataMutable2D(
        width: Int,
        height: Int,
        format: GlTextureFormat,
        level: Int = 0,
        border: Int = 0,
        data: NeoBuffer? = null
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
        data: NeoBuffer? = null
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

    abstract class Basic(
        override val resource: GlTexture,
        override val target: GlTextureTarget,
        override val handle: GlStateStack.Handle<Int>
    ) : GlBoundTexture {
        override var borderColor: NeoColor
            get() = assertBound { NeoColor.RGBA(glGetTexParameteri(target.glId, GL_TEXTURE_BORDER_COLOR)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_BORDER_COLOR, value.toRGBA()) }
        override var compareMode: GlTextureCompareMode
            get() = assertBound { GlNamed.getEnum<GlTextureCompareMode>(glGetTexParameteri(target.glId, GL_TEXTURE_COMPARE_MODE)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_COMPARE_MODE, value.glId) }
        override var compareFunc: GlAlphaFunction
            get() = assertBound { GlNamed.getEnum<GlAlphaFunction>(glGetTexParameteri(target.glId, GL_TEXTURE_COMPARE_FUNC)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_COMPARE_FUNC, value.glId) }
        override var baseLevel: Int
            get() = assertBound { glGetTexParameteri(target.glId, GL_TEXTURE_BASE_LEVEL) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_BASE_LEVEL, value) }
        override var maxLevel: Int
            get() = assertBound { glGetTexParameteri(target.glId, GL_TEXTURE_MAX_LEVEL) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_MAX_LEVEL, value) }
        override var minLod: Int
            get() = assertBound { glGetTexParameteri(target.glId, GL_TEXTURE_MIN_LOD) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_MIN_LOD, value) }
        override var maxLod: Int
            get() = assertBound { glGetTexParameteri(target.glId, GL_TEXTURE_MAX_LOD) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_MAX_LOD, value) }
        override var lodBias: Int
            get() = assertBound { glGetTexParameteri(target.glId, GL_TEXTURE_LOD_BIAS) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_LOD_BIAS, value) }
        override var minFilter: GlTextureMinFilter
            get() = assertBound { GlNamed.getEnum<GlTextureMinFilter>(glGetTexParameteri(target.glId, GL_TEXTURE_MIN_FILTER)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_MIN_FILTER, value.glId) }
        override var magFilter: GlTextureMagFilter
            get() = assertBound { GlNamed.getEnum<GlTextureMagFilter>(glGetTexParameteri(target.glId, GL_TEXTURE_MAG_FILTER)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_MAG_FILTER, value.glId) }
        override var wrapS: GlTextureWrapMode
            get() = assertBound { GlNamed.getEnum<GlTextureWrapMode>(glGetTexParameteri(target.glId, GL_TEXTURE_WRAP_S)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_WRAP_S, value.glId) }
        override var wrapT: GlTextureWrapMode
            get() = assertBound { GlNamed.getEnum<GlTextureWrapMode>(glGetTexParameteri(target.glId, GL_TEXTURE_WRAP_T)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_WRAP_T, value.glId) }
        override var wrapR: GlTextureWrapMode
            get() = assertBound { GlNamed.getEnum<GlTextureWrapMode>(glGetTexParameteri(target.glId, GL_TEXTURE_WRAP_R)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_WRAP_R, value.glId) }
        override var sizzleR: GlTextureSwizzle
            get() = assertBound { GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_R)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_R, value.glId) }
        override var sizzleG: GlTextureSwizzle
            get() = assertBound { GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_G)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_G, value.glId) }
        override var sizzleB: GlTextureSwizzle
            get() = assertBound { GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_B)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_B, value.glId) }
        override var sizzleA: GlTextureSwizzle
            get() = assertBound { GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_A)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_A, value.glId) }
        override var sizzleRGBA: GlTextureSwizzle
            get() = assertBound { GlNamed.getEnum<GlTextureSwizzle>(glGetTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_RGBA)) }
            set(value) = assertBound { glTexParameteri(target.glId, GL_TEXTURE_SWIZZLE_RGBA, value.glId) }

        protected abstract fun resize(width: Int, height: Int, depth: Int, format: GlTextureFormat)

        override fun textureDataMutable1D(
            width: Int,
            format: GlTextureFormat,
            level: Int,
            border: Int,
            data: NeoBuffer?
        ) {
            assertBound {
                glTexImage1D(
                    target.glId,
                    level,
                    format.internalId,
                    width,
                    border,
                    format.glId,
                    format.type,
                    data?.address ?: 0
                )
                resize(width, -1, -1, format)
            }
        }

        override fun textureDataMutable2D(
            width: Int,
            height: Int,
            format: GlTextureFormat,
            level: Int,
            border: Int,
            data: NeoBuffer?
        ) {
            assertBound {
                glTexImage2D(
                    target.glId,
                    level,
                    format.internalId,
                    width,
                    height,
                    border,
                    format.glId,
                    format.type,
                    data?.address ?: 0
                )
                resize(width, height, -1, format)
            }
        }

        override fun textureDataMutable2DMultisample(
            width: Int,
            height: Int,
            format: GlTextureFormat,
            samples: Int,
            fixedSampleLocations: Boolean
        ) {
            assertBound {
                glTexImage2DMultisample(
                    target.glId,
                    samples,
                    format.internalId,
                    width,
                    height,
                    fixedSampleLocations
                )
                resize(width, height, -1, format)
            }
        }

        override fun textureDataMutable3D(
            width: Int,
            height: Int,
            depth: Int,
            format: GlTextureFormat,
            level: Int,
            border: Int,
            data: NeoBuffer?
        ) {
            assertBound {
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
                    data?.address ?: 0
                )
                resize(width, height, depth, format)
            }
        }

        override fun textureDataMutable3DMultisample(
            width: Int,
            height: Int,
            depth: Int,
            format: GlTextureFormat,
            samples: Int,
            fixedSampleLocations: Boolean
        ) {
            assertBound {
                glTexImage3DMultisample(
                    target.glId,
                    samples,
                    format.internalId,
                    width,
                    height,
                    depth,
                    fixedSampleLocations
                )
                resize(width, height, depth, format)
            }
        }

        override fun textureDataImmutable1D(
            width: Int,
            format: GlTextureFormat,
            levels: Int
        ) {
            assertBound {
                glTexStorage1D(target.glId, levels, format.internalId, width)
                resize(width, -1, -1, format)
            }
        }

        override fun textureDataImmutable2D(
            width: Int,
            height: Int,
            format: GlTextureFormat,
            levels: Int
        ) {
            assertBound {
                glTexStorage2D(target.glId, levels, format.internalId, width, height)
                resize(width, height, -1, format)
            }
        }

        override fun textureDataImmutable2DMultisample(
            width: Int,
            height: Int,
            format: GlTextureFormat,
            samples: Int,
            fixedSampleLocations: Boolean
        ) {
            assertBound {
                glTexStorage2DMultisample(target.glId, samples, format.internalId, width, height, fixedSampleLocations)
                resize(width, height, -1, format)
            }
        }

        override fun textureDataImmutable3D(
            width: Int,
            height: Int,
            depth: Int,
            format: GlTextureFormat,
            levels: Int
        ) {
            assertBound {
                glTexStorage3D(target.glId, levels, format.internalId, width, height, depth)
                resize(width, height, depth, format)
            }
        }

        override fun textureDataImmutable3DMultisample(
            width: Int,
            height: Int,
            depth: Int,
            format: GlTextureFormat,
            samples: Int,
            fixedSampleLocations: Boolean
        ) {
            assertBound {
                glTexStorage3DMultisample(
                    target.glId,
                    samples,
                    format.internalId,
                    width,
                    height,
                    depth,
                    fixedSampleLocations
                )
                resize(width, height, depth, format)
            }
        }
    }
}