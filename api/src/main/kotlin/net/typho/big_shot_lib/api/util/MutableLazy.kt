package net.typho.big_shot_lib.api.util

import com.google.common.util.concurrent.Striped.lock
import java.io.Serializable
import kotlin.reflect.KProperty

fun <T> mutableLazy(initializer: () -> T): MutableLazy<T> = SynchronizedMutableLazyImpl(initializer)

fun <T> mutableLazy(lock: Any?, initializer: () -> T): MutableLazy<T> = SynchronizedMutableLazyImpl(initializer, lock)

internal object UninitializedValue

abstract class MutableLazy<out T> : Lazy<T> {
    abstract override var value: @UnsafeVariance T

    abstract operator fun <T> setValue(thisRef: Any?, property: KProperty<*>, value: T)
}

private class SynchronizedMutableLazyImpl<out T>(initializer: () -> T, lock: Any? = null) : MutableLazy<T>(), Serializable {
    private var initializer: (() -> T)? = initializer

    @Volatile
    private var _value: Any? = UninitializedValue

    // final field to ensure safe publication of 'SynchronizedLazyImpl' itself through
    // var lazy = lazy() {}
    private val lock = lock ?: this

    override var value: @UnsafeVariance T
        get() {
            val _v1 = _value
            if (_v1 !== UninitializedValue) {
                @Suppress("UNCHECKED_CAST")
                return _v1 as T
            }

            return synchronized(lock) {
                val _v2 = _value
                if (_v2 !== UninitializedValue) {
                    @Suppress("UNCHECKED_CAST") (_v2 as T)
                } else {
                    val typedValue = initializer!!()
                    _value = typedValue
                    initializer = null
                    typedValue
                }
            }
        }
        set(value) {
            _value = value
        }

    override fun <T> setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _value = value
    }

    override fun isInitialized(): Boolean = _value !== UninitializedValue

    override fun toString(): String = if (isInitialized()) value.toString() else "Lazy value not initialized yet."
}