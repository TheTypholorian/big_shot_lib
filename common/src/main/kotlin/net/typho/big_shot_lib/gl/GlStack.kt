package net.typho.big_shot_lib.gl

import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.gl.state.GlCapability
import net.typho.big_shot_lib.gl.state.GlState
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

    @Suppress("UNCHECKED")
    fun <T : GlState.Value, S : GlState<T>> set(value: T) {
        set(value.getType() as S, value)
    }

    fun <T, S : GlState<T>> set(state: S, value: T) {
        state.set(value)
        states.put(state, value as Any)
        defaultStates.computeIfAbsent(state) { it.default() as Any }
    }

    fun enable(cap: GlCapability) {
        cap.enable()
        capabilities.put(cap, true)
    }

    fun disable(cap: GlCapability) {
        cap.disable()
        capabilities.put(cap, false)
    }

    fun restoreDefaultCapabilities() {
        for (entry in capabilities) {
            if (entry.key.default) {
                entry.key.enable()
            } else {
                entry.key.disable()
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