package net.typho.big_shot_lib.gl

import net.typho.big_shot_lib.api.IIndexedBuffer
import net.typho.big_shot_lib.gl.resource.ExtraUnbind
import net.typho.big_shot_lib.gl.resource.GlIndexedBufferType
import net.typho.big_shot_lib.gl.resource.GlResourceInstance
import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.gl.state.GlCapability
import net.typho.big_shot_lib.gl.state.GlState
import java.lang.AutoCloseable

class GlStack : AutoCloseable {
    @JvmField
    val boundMap = HashMap<GlResourceType, GlResourceInstance>()
    @JvmField
    val defaultStates = HashMap<GlState<*>, Any>()
    @JvmField
    val states = HashMap<GlState<*>, Any>()
    @JvmField
    val capabilities = HashMap<GlCapability, Boolean>()
    @JvmField
    val boundBases = HashMap<GlIndexedBufferType, HashMap<Int, IIndexedBuffer>>()

    fun bind(resource: GlResourceInstance) {
        val bound = boundMap.put(resource.type(), resource)

        if (bound is ExtraUnbind) {
            bound.unbindExtra()
        }
    }

    fun bindBase(resource: IIndexedBuffer, index: Int) {
        boundBases.computeIfAbsent(resource.type()) { HashMap() }.put(index, resource)
    }

    @Suppress("UNCHECKED_CAST")
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

        boundBases.forEach { type ->
            type.value.forEach { base ->
                base.value.unbindBase(base.key)
            }
        }
        boundBases.clear()

        restoreDefaultStates()
        restoreDefaultCapabilities()
    }
}