package net.typho.big_shot_lib.api.client.opengl.shaders

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.util.VertexFormatUtil
import net.typho.big_shot_lib.api.errors.ResourceNotFoundException
import net.typho.big_shot_lib.api.util.resources.NeoFileToIdConverter
import net.typho.big_shot_lib.api.util.resources.NeoResourceManager
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.resources.ResourceRegistry

object NeoShaderRegistry : ResourceRegistry<NeoShader>(BigShotApi.id("shaders"), NeoFileToIdConverter.json("neo/shaders")) {
    override fun decode(
        location: ResourceIdentifier,
        json: JsonObject,
        manager: NeoResourceManager
    ): NeoShader {
        val format = json.get("format") ?: throw JsonParseException("Shader $location is missing vertex format")
        val sourcesObject = json.getAsJsonObject("sources") ?: throw JsonParseException("Shader $location is missing sources")
        val sources = sourcesObject.keySet().map { ShaderSourceType.valueOf(it.uppercase()) }.toSet()
        val builtinDynamicBuffers = json.getAsJsonArray("builtinDynamicBuffers")
            ?.map { ResourceIdentifier(it.asString) }
            ?.toSet() ?: setOf()
        val disabledDynamicBuffers = json.getAsJsonArray("disabledDynamicBuffers")
            ?.map { ResourceIdentifier(it.asString) }
            ?.toSet() ?: setOf()

        val builder = NeoShader.Builder(
            ShaderProgramKey(
                ShaderLoaderType.BIG_SHOT,
                location,
                VertexFormatUtil.fromJson(format),
                sources,
                builtinDynamicBuffers,
                disabledDynamicBuffers
            )
        )
        val resolves = ShaderFileResolver.ResourceBacked(manager)

        for (source in sources) {
            val file = sourcesObject.getAsJsonPrimitive(source.name.lowercase()).asString

            builder.attach(
                source,
                resolves.loadFile("$file.${source.extension}", location.toString(), false)
                    ?: throw ResourceNotFoundException("Couldn't find shader file $file, requested by $location. Searched in ${ShaderFileResolver.directories}"),
                resolves
            )
        }

        return builder.build()
    }
}