package net.typho.big_shot_lib.api.util.content

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.typho.big_shot_lib.api.util.resource.RegisteredResource
import java.util.function.Consumer

sealed interface RegisteredObject<T : Any> : RegisteredResource<T>, ItemLike {
    /**
     * @throws IllegalStateException If [isRegistered] returns false
     */
    fun get(): T

    fun isRegistered(): Boolean

    fun addListener(out: Consumer<T>): RegisteredObject<T>

    override fun asItem(): Item {
        val value = get()
        return (value as? ItemLike ?: throw ClassCastException("$value (id $location) is not an ItemLike")).asItem()
    }

    data class Immediate<T : Any>(
        override val key: ResourceKey<T>,
        private val value: T
    ) : RegisteredObject<T> {
        override fun get() = value

        override fun isRegistered() = true

        override fun addListener(out: Consumer<T>): RegisteredObject<T> {
            out.accept(value)
            return this
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is RegisteredObject<*>) return false

            if (location != other.location) return false

            return true
        }

        override fun hashCode(): Int {
            return location.hashCode()
        }
    }

    data class Late<T : Any>(
        override val key: ResourceKey<T>,
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

        override fun addListener(out: Consumer<T>): RegisteredObject<T> {
            value?.let { out.accept(it) } ?: listeners.add(out)
            return this
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is RegisteredObject<*>) return false

            if (location != other.location) return false

            return true
        }

        override fun hashCode(): Int {
            return location.hashCode()
        }
    }
}