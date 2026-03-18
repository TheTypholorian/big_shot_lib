package net.typho.big_shot_lib.impl.util

@Suppress("UNCHECKED_CAST")
fun <V> Any.setExtensionValue(value: V) {
    (this as MutableExtension<V>).`big_shot_lib$extension_value` = value
}

internal interface MutableExtension<V> : ImmutableExtension<V> {
    override var `big_shot_lib$extension_value`: V
}