package net.typho.big_shot_lib.impl.util

class VoidMap<K, V> : AbstractMutableMap<K, V>() {
    override fun put(key: K, value: V): V? {
        return null
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = HashSet()
}