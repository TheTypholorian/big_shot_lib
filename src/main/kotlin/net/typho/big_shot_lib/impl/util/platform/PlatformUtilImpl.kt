package net.typho.big_shot_lib.impl.util.platform

//? fabric {
/*import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.CustomValue
*///? } neoforge {
import net.minecraft.core.Registry
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.neoforge.registries.RegisterEvent
import net.typho.big_shot_lib.api.client.util.Registrar
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.RegisteredObject
//? }

import net.typho.big_shot_lib.api.util.platform.ModContainer
import net.typho.big_shot_lib.api.util.platform.ModLoader
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.impl.mojang
import java.nio.file.Path

object PlatformUtilImpl : PlatformUtil {
    //? fabric {
    /*override val loader = ModLoader.FABRIC
    override val mods: Collection<ModContainer>
        get() = FabricLoader.getInstance().allMods.map { ModContainerImpl(it) }
    override val configPath: Path
        get() = FabricLoader.getInstance().configDir

    override fun isDevEnv(): Boolean {
        return FabricLoader.getInstance().isDevelopmentEnvironment
    }

    @JvmRecord
    data class ModContainerImpl(
        @JvmField
        val inner: net.fabricmc.loader.api.ModContainer
    ) : ModContainer {
        override val modId: String
            get() = inner.metadata.id
        override val modName: String?
            get() = inner.metadata.name
        override val description: String?
            get() = inner.metadata.description
        override val version: String
            get() = inner.metadata.version.friendlyString

        companion object {
            @JvmStatic
            fun customValueToNormalValue(value: CustomValue): Any? {
                return when (value.type) {
                    CustomValue.CvType.OBJECT -> customValueMapToNormalMap(value.asObject)
                    CustomValue.CvType.ARRAY -> value.asArray.map(::customValueToNormalValue)
                    CustomValue.CvType.STRING -> value.toString()
                    CustomValue.CvType.NUMBER -> value.asNumber
                    CustomValue.CvType.BOOLEAN -> value.asBoolean
                    CustomValue.CvType.NULL -> null
                }
            }

            @JvmStatic
            fun customValueMapToNormalMap(map: Iterable<Map.Entry<String, CustomValue>>): Map<String, Any?> {
                return map.associate { it.key to customValueToNormalValue(it.value) }
            }
        }
    }
    *///? } neoforge {
    override val loader = ModLoader.NEOFORGE
    override val mods: Collection<ModContainer>
        get() = ModList.get().sortedMods.map { ModContainerImpl(it) }
    override val configPath: Path
        get() = FMLPaths.CONFIGDIR.get()

    override fun isDevEnv(): Boolean {
        //? if <1.21.9 {
        return !FMLLoader.isProduction()
        //? } else {
        /*return !FMLLoader.getCurrent().isProduction
        *///? }
    }

    @JvmRecord
    data class ModContainerImpl(
        @JvmField
        val inner: net.neoforged.fml.ModContainer
    ) : ModContainer {
        override val modId: String
            get() = inner.modId
        override val modName: String?
            get() = inner.modInfo.displayName
        override val description: String?
            get() = inner.modInfo.description
        override val version: String
            get() = inner.modInfo.version.toString()
    }

    override fun createRegistrar(): Registrar {
        return NeoForgeRegistrar()
    }

    class NeoForgeRegisteredObject<T : Any>(
        override val registry: NeoResourceKey<out Registry<T>>,
        override val key: NeoIdentifier,
        override val value: T
    ) : RegisteredObject<T> {
        @JvmField
        var registered = false

        override fun isRegistered() = registered

        fun register(event: RegisterEvent) {
            val mojKey = registry.mojang

            if (event.registryKey == mojKey) {
                registered = true
                event.register(mojKey, key.mojang) { value }
            }
        }
    }

    class NeoForgeRegistrar : Registrar {
        @JvmField
        val queue = arrayListOf<RegistryPair<*>>()

        data class RegistryPair<T : Any>(
            @JvmField
            val registry: NeoResourceKey<out Registry<T>>,
            @JvmField
            val values: MutableList<NeoForgeRegisteredObject<T>>
        ) {
            @Suppress("UNCHECKED_CAST")
            fun register(id: NeoIdentifier, value: Any): RegisteredObject<T> {
                return NeoForgeRegisteredObject(registry, id, value as T).also { values.add(it) }
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> register(
            registry: NeoRegistry<out Registry<T>>,
            id: NeoIdentifier,
            value: T
        ): RegisteredObject<T> {
            return (queue.firstOrNull { it.registry == registry.key } ?: RegistryPair(registry.key, arrayListOf()).also { queue.add(it) }).register(id, value) as RegisteredObject<T>
        }
    }
    //? }
}