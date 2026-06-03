package net.typho.big_shot_lib.impl.util.platform

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.util.content.RegisteredObject
import java.util.function.Consumer

class RegisteredObjectImpl<T : Any>(
    override val location: Identifier,
    @JvmField
    val constructor: () -> T
) : RegisteredObject<T> {
    @JvmField
    val listeners = arrayListOf<Consumer<T>>()
    var value: T? = null
        set(value) {
            field = value

            if (value != null) {
                for (consumer in listeners) {
                    consumer.accept(value)
                }
            }
        }

    override fun get(): T {
        return value ?: throw IllegalStateException("Registered Object $location has not been registered yet")
    }

    override fun isRegistered(): Boolean {
        return value != null
    }

    override fun addListener(out: Consumer<T>) {
        value?.let { out.accept(it) } ?: listeners.add(out)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RegisteredObjectImpl<*>) return false

        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        return location.hashCode()
    }
}