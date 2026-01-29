package net.typho.big_shot_lib.buffers

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.textures.TextureFormat
import org.lwjgl.opengl.GL11.glDeleteTextures
import org.lwjgl.opengl.GL30.*

open class NeoRenderBuffer(
    protected val location: ResourceLocation,
    protected val id: Int,
    protected val format: TextureFormat
) : IRenderBuffer {
    constructor(
        location: ResourceLocation,
        format: TextureFormat
    ) : this(location, glGenRenderbuffers(), format)

    init {
        type().label(id(), location().toString())
    }

    override fun release() {
        glDeleteTextures(id)
    }

    override fun location() = location

    override fun type() = GlResourceType.RENDERBUFFER

    override fun id() = id

    override fun getFormat() = format

    override fun attachToFramebuffer(attachment: Int, target: Int) {
        glFramebufferRenderbuffer(
            target,
            attachment,
            type().glName,
            id()
        )
    }

    override fun resize(width: Int, height: Int) {
        bind()
        glRenderbufferStorage(
            type().glName,
            getFormat().id,
            width,
            height
        )
        unbind()
    }
}