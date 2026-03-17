package net.typho.big_shot_lib.impl.util.platform

//? fabric {
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.CustomValue
//? } neoforge {
/*import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader
*///? }

import net.typho.big_shot_lib.api.util.platform.ModContainer
import net.typho.big_shot_lib.api.util.platform.ModLoader
import net.typho.big_shot_lib.api.util.platform.PlatformUtil

object PlatformUtilImpl : PlatformUtil {
    //? fabric {
    override val loader = ModLoader.FABRIC
    override val mods: Collection<ModContainer>
        get() = FabricLoader.getInstance().allMods.map { ModContainerImpl(it) }

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
        override val displayName: String
            get() = inner.metadata.name
        override val description: String
            get() = inner.metadata.description
        override val version: String
            get() = inner.metadata.version.friendlyString
        override val customData: Map<String, Any?>
            get() = customValueMapToNormalMap(inner.metadata.customValues.entries)

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
    //? } neoforge {
    /*override val loader = ModLoader.NEOFORGE
    override val mods: Collection<ModContainer>
        get() = ModList.get().sortedMods.map { ModContainerImpl(it) }

    override fun isDevEnv(): Boolean {
        return !FMLLoader.isProduction()
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
    *///? }
}