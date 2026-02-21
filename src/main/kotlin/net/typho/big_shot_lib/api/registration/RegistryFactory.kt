package net.typho.big_shot_lib.api.registration

import net.minecraft.core.Registry

interface RegistryFactory {
    fun <T> register(registry: Registry<T>): Registry<T>
}