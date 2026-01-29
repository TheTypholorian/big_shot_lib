package net.typho.big_shot_lib.buffers

import net.typho.big_shot_lib.gl.resource.GlIndexedBufferType

interface IIndexedBuffer : IBuffer {
    override fun type(): GlIndexedBufferType

    fun bindBase(index: Int) {
        type().bindBase(id(), index)
    }

    fun unbindBase(index: Int) {
        type().unbindBase(index)
    }
}