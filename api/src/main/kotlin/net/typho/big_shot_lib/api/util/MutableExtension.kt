package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.plugin.Namespace

@Suppress("UNCHECKED_CAST")
fun <V> Any.setExtensionValue(value: V) {
    (this as MutableExtension<V>).extensionValue = value
}

@Namespace(BigShotApi.MOD_ID)
interface MutableExtension<V> : ImmutableExtension<V> {
    override var extensionValue: V
}