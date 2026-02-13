package net.typho.big_shot_lib.api.client.rendering.shaders

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import net.typho.big_shot_lib.api.client.rendering.util.VertexFormatUtil
import net.typho.big_shot_lib.api.errors.ResourceNotFoundException
import net.typho.big_shot_lib.api.services.ResourceManagerWrapper
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.resources.ResourceRegistry

object GlShaderRegistry : ResourceRegistry<GlShader>("shaders") {
    override fun decode(
        id: ResourceIdentifier,
        json: JsonObject,
        manager: ResourceManagerWrapper
    ): GlShader {
        val format = json.get("format") ?: throw JsonParseException("Shader $id is missing vertex format")
        val sourcesObject = json.getAsJsonObject("sources") ?: throw JsonParseException("Shader $id is missing sources")
        val sources = sourcesObject.keySet().map { ShaderSourceType.valueOf(it.uppercase()) } .toSet()

        val builder = GlShader.Builder(ShaderProgramKey(
            ShaderLoaderType.BIG_SHOT,
            id,
            VertexFormatUtil.fromJson(format),
            sources
        ))
        val resolves = ShaderFileResolver.ResourceBacked(manager)

        for (source in sources) {
            val file = sourcesObject.getAsJsonPrimitive(source.name.lowercase()).asString

            builder.attach(
                source,
                resolves.loadFile(
                    "$file.${source.extension}",
                    id.toString()
                ) ?: throw ResourceNotFoundException("Couldn't find shader file $file, requested by $id. Searched in ${ShaderFileResolver.includeKeys}"),
                resolves
            )
        }

        return builder.build()
    }
}