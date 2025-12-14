package net.typho.big_shot_lib.api

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.GlResourceType
import net.typho.big_shot_lib.gl.TextureFormat
import net.typho.big_shot_lib.gl.Unbindable
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL32.glFramebufferTexture
import org.lwjgl.system.MemoryUtil

open class NeoTexture(
    protected val location: ResourceLocation,
    protected val type: GlResourceType,
    protected val id: Int,
    protected val format: TextureFormat
) : ITexture {
    constructor(
        location: ResourceLocation,
        type: GlResourceType,
        format: TextureFormat
    ) : this(location, type, glGenTextures(), format)

    override fun bind(): Unbindable {
        type().bind(id)
        return Unbindable.of(this)
    }

    override fun release() {
        glDeleteTextures(id)
    }

    override fun location() = location

    override fun type() = type

    override fun id() = id

    override fun format() = format

    override fun attach(attachment: Int, target: Int) {
        glFramebufferTexture(
            target,
            attachment,
            id(),
            0
        )
    }

    override fun resize2D(width: Int, height: Int) {
        bind().use {
            glTexImage2D(
                type().glName,
                0,
                format().internal,
                width,
                height,
                0,
                format().id,
                GL_UNSIGNED_BYTE,
                MemoryUtil.NULL
            )
        }
    }
}