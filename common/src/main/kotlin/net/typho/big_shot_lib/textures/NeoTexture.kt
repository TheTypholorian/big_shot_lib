package net.typho.big_shot_lib.textures

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.GlResourceType
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glFramebufferTexture2D
import org.lwjgl.system.MemoryUtil

open class NeoTexture(
    protected var location: ResourceLocation?,
    protected var id: Int? = null
) : ITextureSettings.Storage(), ITexture {
    constructor(
        location: ResourceLocation?,
        settings: ITextureSettings
    ) : this(location, glGenTextures()) {
        copy(settings)
    }

    override fun release() {
        id?.let(::glDeleteTextures)
        id = null
    }

    override fun location() = location

    override fun type() = GlResourceType.TEXTURE_2D

    override fun id() = requireNotNull(id) { "Texture ${location()} has already been released" }

    override fun attachToFramebuffer(attachment: Int, target: Int) {
        glFramebufferTexture2D(
            target,
            attachment,
            type().glName,
            id(),
            0
        )
    }

    protected fun uploadSettings() {
        bind()
        glTexImage2D(
            type().glName,
            0,
            getFormat().internal,
            width!!,
            height!!,
            0,
            getFormat().id,
            getFormat().type,
            MemoryUtil.NULL
        )
        unbind()
    }

    override fun setSize(width: Int, height: Int): NeoTexture {
        super.setSize(width, height)

        uploadSettings()

        return this
    }

    override fun resize(width: Int, height: Int) {
        setSize(width, height)
    }

    override fun setFormat(format: TextureFormat): NeoTexture {
        super.setFormat(format)

        uploadSettings()

        return this
    }

    override fun setInterpolation(min: InterpolationType, mag: InterpolationType): NeoTexture {
        super.setInterpolation(min, mag)

        bind()
        glTexParameteri(type().glName, GL_TEXTURE_MIN_FILTER, min.id)
        glTexParameteri(type().glName, GL_TEXTURE_MAG_FILTER, mag.id)
        unbind()

        return this
    }

    override fun setWrapping(s: WrappingType, t: WrappingType): NeoTexture {
        super.setWrapping(s, t)

        bind()
        glTexParameteri(type().glName, GL_TEXTURE_WRAP_S, s.id)
        glTexParameteri(type().glName, GL_TEXTURE_WRAP_T, t.id)
        unbind()

        return this
    }

    override fun copy(other: ITextureSettings): NeoTexture {
        width = other.getWidth()
        height = other.getHeight()
        format = other.getFormat()
        uploadSettings()
        setInterpolation(other.getMinInterpolation(), other.getMagInterpolation())
        setWrapping(other.getSWrapping(), other.getTWrapping())
        return this
    }
}