package net.typho.big_shot_lib.api

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.GlResourceType
import net.typho.big_shot_lib.gl.TextureFormat
import net.typho.big_shot_lib.gl.Unbindable
import org.lwjgl.opengl.GL11.glDeleteTextures
import org.lwjgl.opengl.GL30.*

open class NeoRenderBuffer(
    protected val location: ResourceLocation,
    protected val id: Int,
    protected val format: TextureFormat
) : ITexture {
    constructor(
        location: ResourceLocation,
        format: TextureFormat
    ) : this(location, glGenRenderbuffers(), format)

    override fun bind(): Unbindable<NeoRenderBuffer> {
        type().bind(id)
        return Unbindable.of(this)
    }

    override fun release() {
        glDeleteTextures(id)
    }

    override fun location() = location

    override fun type() = GlResourceType.RENDERBUFFER

    override fun id() = id

    override fun format() = format

    override fun attach(attachment: Int, target: Int) {
        glFramebufferRenderbuffer(
            target,
            attachment,
            type().glName,
            id()
        )
    }

    override fun resize2D(width: Int, height: Int) {
        bind().use {
            glRenderbufferStorage(
                type().glName,
                format().id,
                width,
                height
            )
        }
    }
}