package net.typho.big_shot_lib.impl.util

import net.neoforged.fml.ModList
import net.typho.big_shot_lib.api.services.ModContainer
import net.typho.big_shot_lib.api.services.PlatformUtil
import net.typho.big_shot_lib.api.util.ModLoader
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import kotlin.reflect.full.companionObjectInstance

class NeoForgePlatformUtilImpl : PlatformUtil {
    override val loader = ModLoader.NEOFORGE
    override val mods: Collection<ModContainer>
        get() = ModList.get().sortedMods.map { ModContainerImpl(it) }

    @JvmRecord
    data class ModContainerImpl(
        @JvmField
        val inner: net.neoforged.fml.ModContainer
    ) : ModContainer {
        override val modId: String
            get() = inner.modId
        override val displayName: String
            get() = inner.modInfo.displayName
        override val description: String
            get() = inner.modInfo.description
        override val version: String
            get() = inner.modInfo.version.toString()
        override val customData: Map<String, Any>
            get() = inner.modInfo.modProperties

        override fun <T> loadEntrypoint(id: ResourceIdentifier, cls: Class<T>): T? {
            val entrypoints = inner.modInfo.modProperties["entrypoints"] as? Map<*, *> ?: return null
            val name = entrypoints[id.toString()] as? String ?: return null

            val found = Class.forName(name, true, ModContainerImpl::class.java.classLoader)

            found.kotlin.objectInstance?.let {
                if (cls.isAssignableFrom(it.javaClass)) {
                    return it as T
                }
            }

            found.kotlin.companionObjectInstance?.let {
                if (cls.isAssignableFrom(it.javaClass)) {
                    return it as T
                }
            }

            if (!cls.isAssignableFrom(found)) {
                throw IllegalStateException("$found does not inherit $cls for entrypoint $id")
            }

            found.kotlin.constructors.firstOrNull { it.parameters.isEmpty() }?.let { return it.call() as T }

            val constructor = found.getDeclaredConstructor()
            constructor.isAccessible = true
            return constructor.newInstance() as T
        }
    }
}