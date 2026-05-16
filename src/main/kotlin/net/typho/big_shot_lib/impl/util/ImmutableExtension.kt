package net.typho.big_shot_lib.impl.util

@Suppress("UNCHECKED_CAST")
inline fun <reified V> Any.getExtensionValue(): V {
    return if (this is V) {
        this
    } else {
        val value = (this as ImmutableExtension<*>).`big_shot_lib$extension_value`
        value as? V ?: throw ClassCastException("Casting $this.$value to ${V::class.simpleName}")
    }
}

@Suppress("UNCHECKED_CAST")
fun <V> Any.getExtensionValue(cls: Class<V>): V {
    return if (cls.isInstance(this)) {
        this as V
    } else {
        val value = (this as ImmutableExtension<*>).`big_shot_lib$extension_value`
        value as? V ?: throw ClassCastException("Casting $this.$value to ${cls.simpleName}")
    }
}

interface ImmutableExtension<V> {
    val `big_shot_lib$extension_value`: V
}