package net.typho.big_shot_lib.gl

import java.lang.AutoCloseable

interface Unbindable : AutoCloseable {
    fun resource(): GlResourceInstance

    fun unbind() = resource().type()!!.unbind()

    override fun close() = unbind()

    companion object {
        fun of(instance: GlResourceInstance) = object : Unbindable {
            override fun resource() = instance
        }
    }
}