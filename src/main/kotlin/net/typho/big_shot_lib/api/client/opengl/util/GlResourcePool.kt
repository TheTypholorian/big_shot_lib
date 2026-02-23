package net.typho.big_shot_lib.api.client.opengl.util

import org.lwjgl.system.NativeResource
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier

open class GlResourcePool<T>(
    @JvmField
    protected val initializer: Function<Int, T>,
    initialSize: Int,
    @JvmField
    protected val fallback: Supplier<T?>,
    @JvmField
    protected val putOnFallback: Boolean
) : NativeResource {
    @JvmField
    protected val resources = HashMap<T, Boolean>()

    init {
        repeat(initialSize) { resources[initializer.apply(it)] = true }
    }

    fun poll(check: Predicate<T> = Predicate { true }): Handle {
        for (entry in resources) {
            if (entry.value && check.test(entry.key)) {
                return Handle(entry.key)
            }
        }

        val value = fallback.get()

        if (value != null) {
            if (putOnFallback) {
                resources[value] = false
            }

            if (check.test(value)) {
                return Handle(value)
            }
        }

        return Handle(null)
    }

    override fun free() {
        for (resource in resources.keys) {
            if (resource is AutoCloseable) {
                resource.close()
            }
        }

        resources.clear()
    }

    inner class Handle(
        @JvmField
        val value: T?
    ) {
        fun release() {
            if (value != null) {
                resources.computeIfPresent(value) { key, value -> true }
            }
        }
    }
}