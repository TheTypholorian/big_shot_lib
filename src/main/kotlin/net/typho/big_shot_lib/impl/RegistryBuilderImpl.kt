package net.typho.big_shot_lib.impl

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.event.RegistryBuilder

//? fabric {
/*import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute

class RegistryBuilderImpl<T>(
    @JvmField
    val key: ResourceKey<Registry<T>>
) : RegistryBuilder<T> {
    override var sync: Boolean = false
    override var defaultKey: Identifier? = null

    fun buildAndRegister(): Registry<T> {
        val builder = if (defaultKey == null) FabricRegistryBuilder.createDefaulted(key, defaultKey) else FabricRegistryBuilder.createSimple(key)

        if (sync) {
            builder.attribute(RegistryAttribute.SYNCED)
        }

        return builder.buildAndRegister()
    }
}
*///? } neoforge {
class RegistryBuilderImpl<T>(
    @JvmField
    val key: ResourceKey<Registry<T>>
) : RegistryBuilder<T> {
    override var sync: Boolean = false
    override var defaultKey: Identifier? = null

    fun build(): net.neoforged.neoforge.registries.RegistryBuilder<T> {
        val builder = net.neoforged.neoforge.registries.RegistryBuilder(key)
        builder.sync(sync)
        defaultKey?.let { builder.defaultKey(it) }
        return builder
    }
}
//? }