package net.typho.big_shot_lib.impl.util

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.CustomValue
import net.typho.big_shot_lib.api.services.ModContainer
import net.typho.big_shot_lib.api.services.PlatformUtil
import net.typho.big_shot_lib.api.util.ModLoader
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

class FabricPlatformUtilImpl : PlatformUtil {
    override val loader = ModLoader.FABRIC
    override val mods: Collection<ModContainer>
        get() = FabricLoader.getInstance().allMods.map { ModContainerImpl(it) }

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

        override fun <T> loadEntrypoint(id: ResourceIdentifier, cls: Class<T>): T? {
            return FabricLoader.getInstance()
                .getEntrypointContainers(id.toString(), cls)
                .firstOrNull { it.provider == inner }
                ?.entrypoint
        }

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
}