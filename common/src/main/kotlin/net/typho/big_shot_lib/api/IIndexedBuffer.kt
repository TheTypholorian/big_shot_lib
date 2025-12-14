package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.GlIndexedBufferType
import net.typho.big_shot_lib.gl.GlResourceStack
import net.typho.big_shot_lib.gl.Unbindable

interface IIndexedBuffer : IBuffer {
    override fun type(): GlIndexedBufferType

    fun bindBase(index: Int): Unbindable<*> {
        type().bindBase(id(), index)
        return Unbindable.ofIndexedBuffer(this, index)
    }

    fun bindBase(stack: GlResourceStack, index: Int) = stack.put(bindBase(index))
}