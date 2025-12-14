package net.typho.big_shot_lib.gl

import java.lang.AutoCloseable
import java.util.*

class GlResourceStack : AutoCloseable {
    val boundMap = HashMap<GlResourceType, Unbindable>()
    val bound = LinkedList<Unbindable>()

    fun put(unbindable: Unbindable): Unbindable? {
        val type = unbindable.resource().type()

        if (type == null) {
            bound.add(unbindable)
        } else {
            return boundMap.put(type, unbindable)
        }

        return null
    }

    override fun close() {
        boundMap.values.forEach { it.unbind() }
        bound.forEach { it.unbind() }
    }
}