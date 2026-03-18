package net.typho.big_shot_lib.impl.util

@Suppress("UNCHECKED_CAST")
fun <V> Any.getExtensionValue(): V = (this as ImmutableExtension<*>).`big_shot_lib$extension_value` as V

internal interface ImmutableExtension<V> {
    val `big_shot_lib$extension_value`: V
}