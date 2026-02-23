package net.typho.big_shot_lib.impl.registration

import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.RegisteredObject
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

@JvmRecord
data class RegisteredObjectImpl<T>(
    override val registry: NeoResourceKey<Registry<T>>,
    override val key: ResourceIdentifier,
    @JvmField
    val value: T
) : RegisteredObject<T> {
    override fun get() = value

    override fun isRegistered() = true
}
