package net.typho.big_shot_lib.api.util

@Suppress("UNCHECKED_CAST")
fun <V> Any.setExtensionValue(value: V) {
    (this as MutableExtension<V>).extensionValue = value
}

interface MutableExtension<V> : ImmutableExtension<V> {
    override var extensionValue: V
}