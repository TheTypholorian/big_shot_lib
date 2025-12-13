package net.typho.big_shot_lib.gl

import java.lang.AutoCloseable

interface Unbindable : AutoCloseable {
    fun getResource(): GlResourceInstance?

    fun unbind() = getResource()!!.type()!!.unbind()

    override fun close() = unbind()
}