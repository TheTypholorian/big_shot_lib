package net.typho.big_shot_lib.shaders

import com.google.gson.JsonParser
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.gl.resource.ShaderType
import net.typho.big_shot_lib.resource.SynchronousReloadListener

object NeoShaderLoader : SynchronousReloadListener {
    @JvmField
    val jsonIdConverter: FileToIdConverter = FileToIdConverter.json(NeoShader.PATH)

    override fun reload(manager: ResourceManager) {
        var loaded = 0

        NeoShader.REGISTRY.values.removeIf { shader ->
            shader.release()
            true
        }

        for (entry in jsonIdConverter.listMatchingResources(manager)) {
            entry.value.openAsReader().use { jsonReader ->
                val id = jsonIdConverter.fileToId(entry.key)

                val json = JsonParser.parseReader(jsonReader).asJsonObject
                val format = json.get("getFormat") ?: throw NullPointerException("Shader $id is missing vertex getFormat")

                val builder = NeoShader.Builder(
                    id,
                    IShader.parseFormat(format)!!,
                    json.has(ShaderType.GEOMETRY.key)
                )

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
                                sourceReader.readText()
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