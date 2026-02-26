package net.typho.big_shot_lib.impl.util

import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLEnvironment
import net.typho.big_shot_lib.api.util.platform.ModContainer
import net.typho.big_shot_lib.api.util.platform.ModLoader
import net.typho.big_shot_lib.api.util.platform.PlatformUtil

class NeoForgePlatformUtilImpl : PlatformUtil {
    override val loader = ModLoader.NEOFORGE
    override val mods: Collection<ModContainer>
        get() = ModList.get().sortedMods.map { ModContainerImpl(it) }

    override fun isDevEnv(): Boolean {
        return !FMLEnvironment.production
    }

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
    }
}