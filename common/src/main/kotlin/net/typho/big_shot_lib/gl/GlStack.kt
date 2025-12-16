package net.typho.big_shot_lib.gl

import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.gl.state.GlCapability
import net.typho.big_shot_lib.gl.state.GlState
import org.lwjgl.opengl.GL11.glDisable
import org.lwjgl.opengl.GL11.glEnable
import java.lang.AutoCloseable
import java.util.*

class GlStack : AutoCloseable {
    val boundMap = HashMap<GlResourceType, Unbindable<*>>()
    val miscBound = LinkedList<Unbindable<*>>()
    val defaultStates = HashMap<GlState<*>, Any>()
    val states = HashMap<GlState<*>, Any>()
    val capabilities = HashMap<GlCapability, Boolean>()

    fun put(unbindable: Unbindable<*>): Unbindable<*>? {
        val type = unbindable.resource().type()

        if (type == null) {
            miscBound.add(unbindable)
        } else {
            return boundMap.put(type, unbindable)
        }

        return null
    }

    fun queryValuesForDefaults(states: List<GlState<*>> = GlState.ALL_STATES) {
        for (state in states) {
            defaultStates.put(state, state.queryValue() as Any)
        }
    }

    fun <T, S : GlState<T>> set(state: S, value: T) {
        state.set(value)
        states.put(state, value as Any)
        defaultStates.computeIfAbsent(state) { it.default() as Any }
    }

    fun enable(cap: GlCapability) {
        glEnable(cap.id)
        capabilities.put(cap, true)
    }

    fun disable(cap: GlCapability) {
        glDisable(cap.id)
        capabilities.put(cap, false)
    }

    fun restoreDefaultCapabilities() {
        for (entry in capabilities) {
            if (entry.key.default) {
                glEnable(entry.key.id)
            } else {
                glDisable(entry.key.id)
            }
        }

        capabilities.replaceAll { cap, value -> cap.default }
    }

    fun restoreDefaultStates() {
        for (entry in defaultStates) {
            entry.key.setCast(entry.value)
        }

        defaultStates.clear()
        states.clear()
    }

    override fun close() {
        boundMap.values.forEach { it.unbind() }
        boundMap.clear()

        miscBound.forEach { it.unbind() }
        miscBound.clear()

        restoreDefaultStates()
        restoreDefaultCapabilities()
    }
}