package net.typho.big_shot_lib.impl.util.platform

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.util.content.RegisteredObject

class RegisteredObjectImpl<T : Any>(
    override val location: Identifier,
    @JvmField
    val constructor: () -> T
) : RegisteredObject<T> {
    @JvmField
    var value: T? = null

    override fun get(): T {
        return value ?: throw IllegalStateException("Registered Object $location has not been registered yet")
    }

    override fun isRegistered(): Boolean {
        return value != null
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