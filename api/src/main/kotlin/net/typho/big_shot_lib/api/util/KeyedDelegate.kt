package net.typho.big_shot_lib.api.util

interface KeyedDelegate<K, V> {
    companion object {
        @JvmStatic
        fun <K, V> of(get: (key: K) -> V): ReadOnly<K, V> {
            return ReadOnly { key -> get(key) }
        }

        @JvmStatic
        fun <K, V> of(get: (key: K) -> V, set: (key: K, value: V) -> Unit): KeyedDelegate<K, V> {
            return object : KeyedDelegate<K, V> {
                override fun get(key: K): V {
                    return get(key)
                }

                override fun set(key: K, value: V) {
                    set(key, value)
                }
            }
        }
    }

    operator fun get(key: K): V

    operator fun set(key: K, value: V)

    fun interface ReadOnly<K, V> {
        operator fun get(key: K): V

        fun withSet(set: (key: K, value: V) -> Unit): KeyedDelegate<K, V> {
            return object : KeyedDelegate<K, V> {
                override fun get(key: K) = this@ReadOnly[key]

                override fun set(key: K, value: V) = set(key, value)
            }
        }
    }
}
