package net.typho.big_shot_lib.buffers

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.GlResourceType
import org.lwjgl.opengl.GL15.*
import java.nio.ByteBuffer

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

    init {
        location()?.let { type().label(id(), it) }
    }

    override fun release() {
        glDeleteBuffers(id)
    }

    override fun location() = location

    override fun type() = type

    override fun id() = id

    override fun usage() = usage

    override fun upload(buffer: ByteBuffer) {
        glBufferData(type().glName, buffer, usage().id)
    }
}