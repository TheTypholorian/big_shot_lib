package net.typho.big_shot_lib.resource

import com.google.gson.JsonParser
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.api.impl.NeoShader
import net.typho.big_shot_lib.gl.resource.ShaderType

object ShaderReloadListener : SynchronousReloadListener {
    val jsonIdConverter: FileToIdConverter = FileToIdConverter.json("neo/shaders")
    val glslIdConverter: FileToIdConverter = FileToIdConverter("neo/shaders", ".glsl")

    override fun reload(manager: ResourceManager) {
        var loaded = 0

        for (entry in jsonIdConverter.listMatchingResources(manager)) {
            entry.value.openAsReader().use { jsonReader ->
                val id = jsonIdConverter.fileToId(entry.key)
                NeoShader.REGISTRY.remove(id)?.release()

                val json = JsonParser.parseReader(jsonReader).asJsonObject
                val builder = NeoShader.Builder(id)

                for (type in ShaderType.entries) {
                    val sourceKeyJson = json.getAsJsonPrimitive(type.key)

                    if (sourceKeyJson != null) {
                        val sourceKey = ResourceLocation.parse(sourceKeyJson.asString)
                        val source = manager.getResourceOrThrow(type.idConverter.idToFile(sourceKey))

                        source.openAsReader().use { sourceReader ->
                            builder.attach(type, sourceReader.readText()) { include ->
                                manager.getResourceOrThrow(glslIdConverter.idToFile(include))
                                    .openAsReader()
                                    .use { reader -> reader.readText() }
                            }
                        }
                    }
                }

                NeoShader.register(builder.build())
                loaded++
            }
        }

        BigShotLib.LOGGER.info("Loaded $loaded shaders")
    }
}