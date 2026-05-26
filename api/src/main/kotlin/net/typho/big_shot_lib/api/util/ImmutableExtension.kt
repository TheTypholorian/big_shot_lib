package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.plugin.Namespace

@Suppress("UNCHECKED_CAST")
inline fun <reified V> Any.getExtensionValue(): V {
    return if (this is V) {
        this
    } else {
        val value = (this as ImmutableExtension<*>).extensionValue
        value as? V ?: throw ClassCastException("Casting $this.$value to ${V::class.simpleName}")
    }
}

@Suppress("UNCHECKED_CAST")
fun <V> Any.getExtensionValue(cls: Class<V>): V {
    return if (cls.isInstance(this)) {
        this as V
    } else {
        val value = (this as ImmutableExtension<*>).extensionValue
        value as? V ?: throw ClassCastException("Casting $this.$value to ${cls.simpleName}")
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified V> Any.getExtensionValueNullable(): V? {
    return when (this) {
        is V -> this
        is ImmutableExtension<*> -> extensionValue as? V
        else -> null
    }
}

@Suppress("UNCHECKED_CAST")
fun <V> Any.getExtensionValueNullable(cls: Class<V>): V? {
    return if (cls.isInstance(this)) {
        this as V
    } else if (this is ImmutableExtension<*>) {
        extensionValue as? V
    } else {
        null
    }
}

@Namespace(BigShotApi.MOD_ID)
interface ImmutableExtension<V> {
    val extensionValue: V
}