package net.typho.big_shot_lib.gl.state

interface GlState<T> {
    fun default(): T

    fun queryValue(): T?

    fun set(value: T)

    @Suppress("UNCHECKED_CAST")
    fun setCast(value: Any) = set(value as T)

    interface Value {
        fun getType(): GlState<*>
    }
}