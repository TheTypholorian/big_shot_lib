package net.typho.big_shot_lib.api.event

import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry

fun interface NewRegistryEvent {
    fun registerRegistries(out: Output)

    interface Output {
        fun <T> register(builder: RegistryBuilder<T>): Registry<T>

        fun <T> register(registry: WritableRegistry<T>): WritableRegistry<T>
    }
}