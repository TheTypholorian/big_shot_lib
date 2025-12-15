package net.typho.big_shot_lib.gl

import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.gl.state.GlState
import java.lang.AutoCloseable
import java.util.*

class GlStack : AutoCloseable {
    val boundMap = HashMap<GlResourceType, Unbindable<*>>()
    val miscBound = LinkedList<Unbindable<*>>()
    val defaultStates = HashMap<GlState<*>, Any>()
    val states = HashMap<GlState<*>, Any>()

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

    fun <T, S : GlState<T>> ensureSet(state: S, value: T) {
        if (states[state] != value) {
            set(state, value)
        }
    }

    fun <T, S : GlState<T>> set(state: S, value: T) {
        state.set(value)
        states.put(state, value as Any)
        defaultStates.computeIfAbsent(state) { it.default() as Any }
    }

    override fun close() {
        boundMap.values.forEach { it.unbind() }
        miscBound.forEach { it.unbind() }

        for (entry in defaultStates) {
            entry.key.setCast(entry.value)
        }

        boundMap.clear()
        miscBound.clear()
        defaultStates.clear()
        states.clear()
    }
}