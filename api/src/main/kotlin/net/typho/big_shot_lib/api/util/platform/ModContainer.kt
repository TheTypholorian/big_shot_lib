package net.typho.big_shot_lib.api.util.platform

import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.io.InputStreamReader
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

interface ModContainer {
    val modId: String
    val modName: String?
    val description: String?
    val version: String

    data class Record(
        override val modId: String,
        override val modName: String?,
        override val description: String?,
        override val version: String
    ) : ModContainer

    companion object {
        @JvmField
        val NEO_CODEC: MapCodec<ModContainer> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("modId").forGetter { container -> container.modId },
                Codec.STRING.optionalFieldOf("modName").forGetter { container -> Optional.ofNullable(container.modName) },
                Codec.STRING.optionalFieldOf("description").forGetter { container -> Optional.ofNullable(container.description) },
                Codec.STRING.fieldOf("version").forGetter { container -> container.version },
            ).apply(it) { modId, modName, description, version -> Record(modId, modName.getOrNull(), description.getOrNull(), version) }
        }

        @JvmStatic
        fun get(cls: Class<*>): ModContainer? {
            return cls.getResourceAsStream("big_shot_lib.mod.json")?.use { NEO_CODEC.codec().parse(JsonOps.INSTANCE, JsonParser.parseReader(InputStreamReader(it))).orThrow }
        }
    }
}