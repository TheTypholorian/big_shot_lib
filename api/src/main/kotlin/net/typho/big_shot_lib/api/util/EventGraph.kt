package net.typho.big_shot_lib.api.util

import java.util.function.Consumer

open class EventGraph<K, T> {
    inner class Event(
        @JvmField
        val id: K,
        @JvmField
        val event: T,
        @JvmField
        val runThisBefore: List<K>,
        @JvmField
        val runThisAfter: List<K>
    )

    @JvmField
    protected val events = mutableListOf<Event>()
    @JvmField
    protected var ordered = true

    fun register(
        id: K,
        event: T
    ) {
        register(id, event, listOf(), listOf())
    }

    fun register(
        id: K,
        event: T,
        runThisBefore: List<K>,
        runThisAfter: List<K>
    ) {
        events.add(Event(id, event, runThisBefore, runThisAfter))
        ordered = false
    }

    fun execute(out: Consumer<T>) {
        resolve().forEach { out.accept(it.event) }
    }

    fun resolve(): List<Event> {
        if (!ordered) {
            val lookup = events.associateBy { it.id }
            val edges = events.associateWith { mutableSetOf<Event>() }
            val incoming = events.associateWith { 0 }.toMutableMap()

            for (event in events) {
                for (before in event.runThisBefore) {
                    val target = lookup[before] ?: continue

                    if (edges[event]!!.add(target)) {
                        incoming[target] = incoming[target]!! + 1
                    }
                }

                for (after in event.runThisAfter) {
                    val source = lookup[after] ?: continue

                    if (edges[source]!!.add(event)) {
                        incoming[event] = incoming[event]!! + 1
                    }
                }
            }

            val queue = ArrayDeque(events.filter { incoming[it] == 0 })
            val result = mutableListOf<Event>()

            while (queue.isNotEmpty()) {
                val event = queue.removeFirst()
                result.add(event)

                for (next in edges[event]!!) {
                    incoming[next] = incoming[next]!! - 1

                    if (incoming[next] == 0) {
                        queue.addLast(next)
                    }
                }
            }

            if (result.size != events.size) {
                throw IllegalStateException("Event graph contains a cycle")
            }

            events.clear()
            events.addAll(result)

            ordered = true
        }

        return events
    }
}