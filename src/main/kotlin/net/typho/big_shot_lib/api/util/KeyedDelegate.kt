package net.typho.big_shot_lib.api.util

data class KeyedDelegate<K, V>(
    private val get: (key: K) -> V,
    private val set: (key: K, value: V) -> Unit,
) {
    operator fun get(key: K) = get.invoke(key)

    operator fun set(key: K, value: V) = set.invoke(key, value)
}
