package net.typho.big_shot_lib.gl

import java.lang.AutoCloseable

interface Unbindable<T : GlResourceInstance> : AutoCloseable {
    fun resource(): T

    fun unbind() = resource().type()!!.unbind()

    override fun close() = unbind()

    companion object {
        fun <T : GlResourceInstance> of(instance: T) = object : Unbindable<T> {
            override fun resource() = instance
        }
    }
}