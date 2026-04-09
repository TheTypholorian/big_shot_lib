package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureCompareMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureMagFilter
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureMinFilter
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureWrapMode
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlSampler
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext
import org.lwjgl.opengl.GL42.*

open class NeoGlSampler(glId: Int, autoFree: Boolean, context: RenderingContext = RenderingContext.get()) : NeoGlResource(GlResourceType.SAMPLER, glId, autoFree, context), GlSampler {
    override var compareMode: GlTextureCompareMode
        get() = GlNamed.getEnum<GlTextureCompareMode>(glGetSamplerParameteri(glId, GL_TEXTURE_COMPARE_MODE))
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_COMPARE_MODE, value.glId)
    override var compareFunc: GlAlphaFunction
        get() = GlNamed.getEnum<GlAlphaFunction>(glGetSamplerParameteri(glId, GL_TEXTURE_COMPARE_FUNC))
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_COMPARE_FUNC, value.glId)
    override var minLod: Int
        get() = glGetSamplerParameteri(glId, GL_TEXTURE_MIN_LOD)
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_MIN_LOD, value)
    override var maxLod: Int
        get() = glGetSamplerParameteri(glId, GL_TEXTURE_MAX_LOD)
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_MAX_LOD, value)
    override var lodBias: Int
        get() = glGetSamplerParameteri(glId, GL_TEXTURE_LOD_BIAS)
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_LOD_BIAS, value)
    override var minFilter: GlTextureMinFilter
        get() = GlNamed.getEnum<GlTextureMinFilter>(glGetSamplerParameteri(glId, GL_TEXTURE_MIN_FILTER))
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_MIN_FILTER, value.glId)
    override var magFilter: GlTextureMagFilter
        get() = GlNamed.getEnum<GlTextureMagFilter>(glGetSamplerParameteri(glId, GL_TEXTURE_MAG_FILTER))
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_MAG_FILTER, value.glId)
    override var wrapS: GlTextureWrapMode
        get() = GlNamed.getEnum<GlTextureWrapMode>(glGetSamplerParameteri(glId, GL_TEXTURE_WRAP_S))
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_WRAP_S, value.glId)
    override var wrapT: GlTextureWrapMode
        get() = GlNamed.getEnum<GlTextureWrapMode>(glGetSamplerParameteri(glId, GL_TEXTURE_WRAP_T))
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_WRAP_T, value.glId)
    override var wrapR: GlTextureWrapMode
        get() = GlNamed.getEnum<GlTextureWrapMode>(glGetSamplerParameteri(glId, GL_TEXTURE_WRAP_R))
        set(value) = glSamplerParameteri(glId, GL_TEXTURE_WRAP_R, value.glId)
    
    constructor() : this(GlResourceType.SAMPLER.create(), true)
}