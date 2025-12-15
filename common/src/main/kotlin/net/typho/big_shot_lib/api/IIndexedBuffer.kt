package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.GlStack
import net.typho.big_shot_lib.gl.Unbindable
import net.typho.big_shot_lib.gl.resource.GlIndexedBufferType

interface IIndexedBuffer : IBuffer {
    override fun type(): GlIndexedBufferType

    fun bindBase(index: Int): Unbindable<*> {
        type().bindBase(id(), index)
        return Unbindable.ofIndexedBuffer(this, index)
    }

    fun bindBase(stack: GlStack, index: Int) = stack.put(bindBase(index))
}