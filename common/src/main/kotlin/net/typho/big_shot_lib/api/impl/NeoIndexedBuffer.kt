package net.typho.big_shot_lib.api.impl

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.IIndexedBuffer
import net.typho.big_shot_lib.gl.resource.BufferUsage
import net.typho.big_shot_lib.gl.resource.GlIndexedBufferType

open class NeoIndexedBuffer : NeoBuffer, IIndexedBuffer {
    constructor(
        location: ResourceLocation?,
        type: GlIndexedBufferType,
        id: Int,
        usage: BufferUsage
    ) : super(location, type, id, usage)

    constructor(
        location: ResourceLocation?,
        type: GlIndexedBufferType,
        usage: BufferUsage
    ) : super(location, type, usage)

    override fun type(): GlIndexedBufferType = type as GlIndexedBufferType
}