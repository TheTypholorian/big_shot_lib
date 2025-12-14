package net.typho.big_shot_lib.api

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.BufferUsage
import net.typho.big_shot_lib.gl.GlResourceType
import net.typho.big_shot_lib.gl.Unbindable
import org.lwjgl.opengl.GL15.*

open class NeoBuffer(
    protected val location: ResourceLocation?,
    protected val type: GlResourceType,
    protected val id: Int,
    protected val usage: BufferUsage
) : IBuffer {
    constructor(
        location: ResourceLocation?,
        type: GlResourceType,
        usage: BufferUsage
    ) : this(location, type, glGenBuffers(), usage)

    override fun release() {
        glDeleteBuffers(id)
    }

    override fun location() = location

    override fun type() = type

    override fun id() = id

    override fun bind(): Unbindable<*> {
        type().bind(id())
        return Unbindable.of(this)
    }

    override fun usage() = usage

    override fun upload(buffer: Long) {
        glBufferData(type().glName, buffer, usage().id)
    }
}