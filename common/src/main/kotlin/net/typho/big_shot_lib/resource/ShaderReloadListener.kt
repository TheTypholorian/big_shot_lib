package net.typho.big_shot_lib.resource

import com.google.gson.JsonParser
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.api.NeoShader
import net.typho.big_shot_lib.gl.ShaderType

object ShaderReloadListener : SynchronousReloadListener {
    val idConverter: FileToIdConverter = FileToIdConverter.json("neo/shaders")

    override fun reload(manager: ResourceManager) {
        var loaded = 0

        for (entry in idConverter.listMatchingResources(manager)) {
            entry.value.openAsReader().use { jsonReader ->
                val id = idConverter.fileToId(entry.key)
                NeoShader.REGISTRY.remove(id)?.release()

                val json = JsonParser.parseReader(jsonReader).asJsonObject
                val builder = NeoShader.Builder()

                for (type in ShaderType.entries) {
                    val sourceKeyJson = json.getAsJsonPrimitive(type.key)

                    if (sourceKeyJson != null) {
                        val sourceKey = ResourceLocation.parse(sourceKeyJson.asString)
                        val source = manager.getResourceOrThrow(type.idConverter.idToFile(sourceKey))

                        source.openAsReader().use { sourceReader ->
                            builder.attach(type, sourceReader.readText())
                        }
                    }
                }

                NeoShader.register(builder.build(id))
                loaded++
            }
        }

        BigShotLib.LOGGER.info("Loaded $loaded shaders")
    }
}