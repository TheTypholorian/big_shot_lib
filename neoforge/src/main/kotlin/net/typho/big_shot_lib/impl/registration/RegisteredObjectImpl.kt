package net.typho.big_shot_lib.impl.registration

import net.minecraft.core.Registry
import net.neoforged.neoforge.registries.DeferredHolder
import net.typho.big_shot_lib.api.registration.RegisteredObject
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

@JvmRecord
data class RegisteredObjectImpl<T : Any>(
    override val registry: NeoResourceKey<Registry<T>>,
    override val key: ResourceIdentifier,
    @JvmField
    val holder: DeferredHolder<*, T>
) : RegisteredObject<T> {
    override fun get(): T = holder.get()

    override fun isRegistered() = holder.isBound
}
