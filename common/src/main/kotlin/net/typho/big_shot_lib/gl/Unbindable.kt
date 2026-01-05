package net.typho.big_shot_lib.gl

import net.typho.big_shot_lib.api.IBuffer
import net.typho.big_shot_lib.gl.resource.GlIndexedBufferType
import net.typho.big_shot_lib.gl.resource.GlResourceInstance
import java.lang.AutoCloseable

interface Unbindable<T : GlResourceInstance> : AutoCloseable {
    fun resource(): T

    fun unbind() = resource().type()!!.unbind()

    override fun close() = unbind()

    companion object {
        @JvmStatic
        fun <T : GlResourceInstance> of(instance: T) = object : Unbindable<T> {
            override fun resource() = instance
        }

        @JvmStatic
        fun <T : IBuffer> ofIndexedBuffer(instance: T, index: Int) = object : Unbindable<T> {
            override fun resource() = instance

            override fun unbind() = (instance.type() as GlIndexedBufferType).unbindBase(index)
        }
    }
}