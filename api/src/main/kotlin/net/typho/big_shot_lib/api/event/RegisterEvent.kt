package net.typho.big_shot_lib.api.event

import com.mojang.serialization.Lifecycle
import net.minecraft.core.RegistrationInfo
import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.typho.big_shot_lib.api.util.content.RegisteredObject
import java.util.*
import java.util.function.Consumer

fun interface RegisterEvent {
    fun register(output: Output)

    interface Output {
        fun <T : Any> begin(key: Identifier, out: Consumer<RegistrationConsumer<T>>)

        fun <T : Any> begin(key: ResourceKey<out Registry<T>>, out: Consumer<RegistrationConsumer<T>>)

        fun <T : Any> begin(registry: Registry<T>, out: Consumer<RegistrationConsumer<T>>)

        fun beginBlocks(out: Consumer<RegistrationConsumer<Block>>) = begin(BuiltInRegistries.BLOCK, out)

        fun beginItems(out: Consumer<RegistrationConsumer<Item>>) = begin(BuiltInRegistries.ITEM, out)

        open class ToRegistry<V : Any>(
            @JvmField
            protected val registry: WritableRegistry<V>
        ) : Output {
            var count = 0
                protected set

            override fun <T : Any> begin(
                key: Identifier,
                out: Consumer<RegistrationConsumer<T>>
            ) {
                begin(ResourceKey.createRegistryKey(key), out)
            }

            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> begin(
                key: ResourceKey<out Registry<T>>,
                out: Consumer<RegistrationConsumer<T>>
            ) {
                if (key == registry.key()) {
                    val info = RegistrationInfo(Optional.empty(), Lifecycle.stable())
                    out.accept(object : RegistrationConsumer<T> {
                        override fun register(key: Identifier, value: T) {
                            registry.register(ResourceKey.create(registry.key(), key), value as V, info)
                            count++
                        }

                        override fun register(key: ResourceKey<out T>, value: T) {
                            registry.register(key as ResourceKey<V>, value as V, info)
                            count++
                        }
                    })
                }
            }

            override fun <T : Any> begin(
                registry: Registry<T>,
                out: Consumer<RegistrationConsumer<T>>
            ) {
                begin(registry.key(), out)
            }
        }
    }

    interface RegistrationConsumer<T : Any> {
        fun register(key: Identifier, value: T)

        fun register(key: ResourceKey<out T>, value: T)

        fun <V : T> register(obj: RegisteredObject<V>) {
            when (obj) {
                is RegisteredObject.Immediate<V> -> {
                    register(obj.key, obj.get())
                }
                is RegisteredObject.Late<V> -> {
                    if (obj.isRegistered()) {
                        register(obj.key, obj.get())
                    } else {
                        val value = obj.constructor.invoke()
                        register(obj.key, value)
                        obj.value = value
                    }
                }
            }
        }
    }
}