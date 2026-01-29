package net.typho.big_shot_lib.gl.stack

import net.typho.big_shot_lib.buffers.IIndexedBuffer
import net.typho.big_shot_lib.gl.resource.GlIndexedBufferType
import net.typho.big_shot_lib.gl.resource.GlResourceInstance
import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.gl.state.GlFlag
import net.typho.big_shot_lib.gl.state.GlState
import java.lang.AutoCloseable
import java.util.*

class GlStack : AutoCloseable, GlBinder {
    private inner class Node : AutoCloseable, GlBinder {
        @JvmField
        val boundMap = HashMap<GlResourceType, GlResourceInstance>()
        @JvmField
        val boundBases = HashMap<GlIndexedBufferType, HashMap<Int, IIndexedBuffer>>()
        @JvmField
        val states = HashMap<GlState<*>, Any>()
        @JvmField
        val flags = HashMap<GlFlag, Boolean>()

        fun pull(other: Node) {
            boundMap.putAll(other.boundMap)
            boundBases.putAll(other.boundBases)
            states.putAll(other.states)
            flags.putAll(other.flags)
        }

        fun reduceToDifference(other: Node) {
        }

        override fun bind(resource: GlResourceInstance) {
            boundMap.put(resource.type(), resource)?.let { bound ->
                if (!bound.canHotswapBind()) {
                    bound.unbind()
                }
            }

            resource.bind()
        }

        override fun bindBase(resource: IIndexedBuffer, index: Int) {
            boundBases.computeIfAbsent(resource.type()) { HashMap() }[index] = resource
            resource.bindBase(index)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : GlState.Value, S : GlState<T>> set(value: T) {
            set(value.getType() as S, value)
        }

        override fun <T, S : GlState<T>> set(state: S, value: T) {
            state.set(value)
            states[state] = value as Any
            defaultStates.computeIfAbsent(state) { it.default() as Any }
        }

        override fun enable(flag: GlFlag) {
            flag.enable()
            flags[flag] = true
        }

        override fun disable(flag: GlFlag) {
            flag.disable()
            flags[flag] = false
        }

        override fun restoreDefaultFlags() {
            for (entry in flags) {
                if (entry.key.default) {
                    entry.key.enable()
                } else {
                    entry.key.disable()
                }
            }

            flags.replaceAll { flag, value -> flag.default }
        }

        override fun restoreDefaultStates() {
            for (entry in defaultStates) {
                entry.key.setCast(entry.value)
            }

            defaultStates.clear()
            states.clear()
        }

        override fun unbindAllResources() {
            boundMap.values.forEach { it.unbind() }
            boundMap.clear()
        }

        override fun unbindAllBases() {
            boundBases.forEach { type ->
                type.value.forEach { base ->
                    base.value.unbindBase(base.key)
                }
            }
            boundBases.clear()
        }

        override fun close() {
            unbindAllResources()
            unbindAllBases()

            restoreDefaultStates()
            restoreDefaultFlags()
        }
    }

    @JvmField
    val defaultStates = HashMap<GlState<*>, Any>()
    private val nodes = LinkedList<Node>()

    init {
        push()
    }

    fun push() {
        nodes.add(Node())
    }

    fun pop() {
        nodes.lastOrNull()?.let { node ->
        }
    }

    override fun bind(resource: GlResourceInstance) {
        TODO("Not yet implemented")
    }

    override fun bindBase(resource: IIndexedBuffer, index: Int) {
        TODO("Not yet implemented")
    }

    override fun <T : GlState.Value, S : GlState<T>> set(value: T) {
        TODO("Not yet implemented")
    }

    override fun <T, S : GlState<T>> set(state: S, value: T) {
        TODO("Not yet implemented")
    }

    override fun enable(flag: GlFlag) {
        TODO("Not yet implemented")
    }

    override fun disable(flag: GlFlag) {
        TODO("Not yet implemented")
    }

    override fun restoreDefaultFlags() {
        TODO("Not yet implemented")
    }

    override fun restoreDefaultStates() {
        TODO("Not yet implemented")
    }

    override fun unbindAllResources() {
        TODO("Not yet implemented")
    }

    override fun unbindAllBases() {
        TODO("Not yet implemented")
    }

    override fun close() {
    }
}