package net.typho.big_shot_lib.resource

import com.google.gson.JsonParser
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.api.IShader
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
                val builder = NeoShader.Builder(id, IShader.parseFormat(json.get("format")!!)!!)

                for (type in ShaderType.entries) {
                    val sourceKeyJson = json.getAsJsonPrimitive(type.key)

                    if (sourceKeyJson != null) {
                        val sourceKey = ResourceLocation.parse(sourceKeyJson.asString)
                        val withExtension = type.idConverter.idToFile(sourceKey)
                        val source = manager.getResourceOrThrow(withExtension)

                        source.openAsReader().use { sourceReader ->
                            builder.attach(
                                type,
                                withExtension.toString(),
                                sourceReader.readText(),
                                { include ->
                                    manager.getResourceOrThrow(glslIdConverter.idToFile(include))
                                        .openAsReader()
                                        .use { reader -> reader.readText() }
                                }
                            )
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