package net.typho.big_shot_lib.api.util

import org.lwjgl.system.NativeResource

open class ResourcePool<R : NativeResource>(
    @JvmField
    protected val initializer: (index: Int) -> R,
    initialSize: Int,
    @JvmField
    protected val fallback: () -> R?,
    @JvmField
    protected val putOnFallback: Boolean
) : NativeResource {
    @JvmField
    protected val resources = arrayListOf<Handle>()

    init {
        repeat(initialSize) { resources.add(Handle(initializer(it), false, true)) }
    }

    fun poll(check: (value: R) -> Boolean = { true }): Handle? {
        for (handle in resources) {
            if (handle.free && check(handle.value)) {
                handle.free = false
                return handle
            }
        }

        val value = fallback()

        if (value != null) {
            if (check(value)) {
                val handle = Handle(value, !putOnFallback, false)

                if (putOnFallback) {
                    resources.add(handle)
                }

                return handle
            }
        }

        return null
    }

    override fun free() {
        resources.forEach { it.value.free() }
        resources.clear()
    }

    inner class Handle(
        @JvmField
        val value: R,
        internal var isTemp: Boolean,
        internal var free: Boolean
    ) : NativeResource {
        override fun free() {
            if (isTemp) {
                value.free()
            } else {
                free = true
            }
        }
    }
}