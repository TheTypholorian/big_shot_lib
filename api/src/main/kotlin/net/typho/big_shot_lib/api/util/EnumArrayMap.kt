package net.typho.big_shot_lib.api.util

import kotlin.enums.enumEntries

inline fun <reified K : Enum<K>, V> enumArrayMapOf(noinline init: (key: K) -> V) = EnumArrayMap(enumEntries<K>(), init)

inline fun <reified K : Enum<K>, V> mutableEnumArrayMapOf(noinline init: (key: K) -> V) = MutableEnumArrayMap(enumEntries<K>(), init)

open class EnumArrayMap<K : Enum<K>, V>(
    private val entries: List<K>,
    init: (key: K) -> V
) {
    protected val array = ArrayList<V>(entries.size)

    init {
        entries.forEach { array[it.ordinal] = init(it) }
    }

    constructor(cls: Class<K>, init: (key: K) -> V) : this(listOf(*cls.enumConstants!!), init)

    operator fun get(key: K) = array[key.ordinal]
}

open class MutableEnumArrayMap<K : Enum<K>, V> : EnumArrayMap<K, V> {
    constructor(entries: List<K>, init: (key: K) -> V) : super(entries, init)

    constructor(cls: Class<K>, init: (key: K) -> V) : super(cls, init)

    operator fun set(key: K, value: V) {
        array[key.ordinal] = value
    }
}