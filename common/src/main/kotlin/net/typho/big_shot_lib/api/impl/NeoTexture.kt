package net.typho.big_shot_lib.api.impl

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.ITexture
import net.typho.big_shot_lib.gl.Unbindable
import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.gl.resource.TextureFormat
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R
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

    init {
        bind().use {
            glTexParameteri(type.glName, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(type.glName, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glTexParameteri(type.glName, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE)
            glTexParameteri(type.glName, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            glTexParameteri(type.glName, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        }
        type().label(id(), location().toString())
    }

    override fun bind(): Unbindable<NeoTexture> {
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
                format().type,
                MemoryUtil.NULL
            )
        }
    }
}