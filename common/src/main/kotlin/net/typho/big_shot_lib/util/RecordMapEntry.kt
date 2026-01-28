package net.typho.big_shot_lib.util

data class RecordMapEntry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V>
