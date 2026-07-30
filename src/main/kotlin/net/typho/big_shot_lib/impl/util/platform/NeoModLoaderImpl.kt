package net.typho.big_shot_lib.impl.util.platform

//? fabric {
/*import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.CustomValue
import net.typho.big_shot_lib.api.util.platform.INeoModLoader
*///? } neoforge {
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import net.typho.big_shot_lib.api.util.platform.INeoModLoader
//? }

import net.typho.big_shot_lib.api.util.platform.ModContainer
import net.typho.big_shot_lib.api.util.platform.ModLoader
import net.typho.big_shot_lib.common.annotation.Environment
import java.nio.file.Path

object NeoModLoaderImpl : INeoModLoader {
    //? fabric {
    /*override val loader = ModLoader.FABRIC
    override val mods: Collection<ModContainer>
        get() = FabricLoader.getInstance().allMods.map { ModContainerImpl(it) }
    override val configPath: Path
        get() = FabricLoader.getInstance().configDir

    override fun isDevEnv(): Boolean {
        return FabricLoader.getInstance().isDevelopmentEnvironment
    }

    override fun isClient(): Boolean {
        return FabricLoader.getInstance().environmentType == EnvType.CLIENT
    }

    @JvmRecord
    data class ModContainerImpl(
        @JvmField
        val inner: net.fabricmc.loader.api.ModContainer
    ) : ModContainer {
        override val id: String
            get() = inner.metadata.id
        override val name: String?
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
    override val backend = ModLoader.NEOFORGE
    override val mods: Collection<ModContainer>
        get() = ModList.get().sortedMods.map { ModContainerImpl(it) }
    override val configPath: Path
        get() = FMLPaths.CONFIGDIR.get()
    override val environment: Environment = when (FMLEnvironment.getDist()) {
        Dist.CLIENT -> Environment.CLIENT
        Dist.DEDICATED_SERVER -> Environment.SERVER
    }

    override fun isDevEnv(): Boolean {
        //? if <1.21.9 {
        /*return !FMLLoader.isProduction()
        *///? } else {
        return !FMLLoader.getCurrent().isProduction
        //? }
    }

    @JvmRecord
    data class ModContainerImpl(
        @JvmField
        val inner: net.neoforged.fml.ModContainer
    ) : ModContainer {
        override val id: String
            get() = inner.modId
        override val name: String?
            get() = inner.modInfo.displayName
        override val description: String?
            get() = inner.modInfo.description
        override val version: String
            get() = inner.modInfo.version.toString()
    }
    //? }
}