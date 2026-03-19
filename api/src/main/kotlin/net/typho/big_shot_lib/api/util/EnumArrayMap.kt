package net.typho.big_shot_lib.api.util

import kotlin.enums.enumEntries

inline fun <reified K : Enum<K>, V> enumArrayMapOf(noinline init: (key: K) -> V) = EnumArrayMap(enumEntries<K>(), init)

inline fun <reified K : Enum<K>, V> mutableEnumArrayMapOf(noinline init: (key: K) -> V) = MutableEnumArrayMap(enumEntries<K>(), init)

open class EnumArrayMap<K : Enum<K>, V>(
    protected val enumEntries: List<K>,
    init: (key: K) -> V
) : Map<K, V> {
    protected val array = ArrayList<V>(enumEntries.size)

    init {
        enumEntries.forEach { array[it.ordinal] = init(it) }
    }

    constructor(cls: Class<K>, init: (key: K) -> V) : this(listOf(*cls.enumConstants!!), init)

    override val size: Int = enumEntries.size
    override val keys: Set<K> = enumEntries.toSet()
    override val values: Collection<V> = array
    override val entries: Set<Map.Entry<K, V>> = enumEntries.zip(array).mapTo(HashSet()) { pair -> object : Map.Entry<K, V> {
        override val key: K = pair.first
        override val value: V
            get() = get(key)
    } }

    override fun isEmpty() = array.isEmpty()

    override fun containsKey(key: K) = keys.contains(key)

    override fun containsValue(value: V) = values.contains(value)

    override operator fun get(key: K) = array[key.ordinal]
}

open class MutableEnumArrayMap<K : Enum<K>, V> : EnumArrayMap<K, V>, MutableMap<K, V> {
    constructor(entries: List<K>, init: (key: K) -> V) : super(entries, init)

    constructor(cls: Class<K>, init: (key: K) -> V) : super(cls, init)

    override val keys: MutableSet<K> = hashSetOf()
    override val values: MutableCollection<V> = array
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = enumEntries.zip(array).mapTo(HashSet()) { pair -> object : MutableMap.MutableEntry<K, V> {
        override val key: K = pair.first
        override val value: V
            get() = get(key)

        override fun setValue(newValue: V): V {
            return put(key, newValue)!!
        }
    } }

    operator fun set(key: K, value: V) {
        array[key.ordinal] = value
    }

    override fun put(key: K, value: V): V? {
        val old = array[key.ordinal]
        array[key.ordinal] = value
        return old
    }

    override fun remove(key: K): V? {
        throw UnsupportedOperationException()
    }

    override fun putAll(from: Map<out K, V>) {
        from.forEach { (k, v) -> this[k] = v }
    }

    override fun clear() {
        throw UnsupportedOperationException()
    }
}