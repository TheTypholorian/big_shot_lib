package net.typho.big_shot_lib.api.client.opengl.shaders

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.typho.big_shot_lib.api.BigShotApi
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
        val builder = NeoShader.Builder(ShaderProgramKey.codec(ShaderLoaderType.BIG_SHOT, location).codec().decode(JsonOps.INSTANCE, json).orThrow.first)
        val files = ShaderFileResolver.ResourceBacked(manager)

        for (source in builder.key.sources) {
            builder.attach(
                source.key,
                files.loadFile(source.value, source.key)
                    ?: throw ResourceNotFoundException("Couldn't find shader file ${source.value}, requested by $location. Searched in ${ShaderFileResolver.directories}"),
                files
            )
        }

        return builder.build()
    }
}