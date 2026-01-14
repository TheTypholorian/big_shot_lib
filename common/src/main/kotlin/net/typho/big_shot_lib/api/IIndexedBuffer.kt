package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.GlStack
import net.typho.big_shot_lib.gl.resource.GlIndexedBufferType

interface IIndexedBuffer : IBuffer {
    override fun type(): GlIndexedBufferType

    fun bindBase(index: Int) {
        type().bindBase(id(), index)
    }

    fun bindBase(stack: GlStack, index: Int) {
        stack.bindBase(this, index)
        bindBase(index)
    }

    fun unbindBase(index: Int) {
        type().unbindBase(index)
    }
}