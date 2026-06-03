package net.typho.big_shot_lib.api.client.rendering

import com.google.gson.JsonParser
import com.mojang.serialization.DataResult
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.event.AddAssetReloadListenersEvent
import net.typho.big_shot_lib.api.client.event.NeoClientEventBus
import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormats
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.util.resource.ResourceRegistry
import net.typho.big_shot_lib.api.util.*
import java.io.BufferedReader

@JvmField
val shaderIncludes = object : ResourceRegistry<String>(
    BigShotApi.id("shaders/include"),
    mutableListOf(),
    mutableListOf(
        FileToIdConverter("neo/shaders/include", ".glsl"),
        FileToIdConverter("shaders/include", ".glsl")
    )
) {
    override fun decode(
        location: Identifier,
        reader: BufferedReader,
        manager: ResourceManager
    ): DataResult<String> {
        var text = reader.readText().trim()

        if (text.startsWith("#version")) {
            text = text.substring(text.indexOf('\n') + 1)
        }

        return DataResult.success(text)
    }
}

@JvmField
val shaderRegistries = enumArrayMapOf<GlShaderType, ResourceRegistry<GlShader>> { shaderType ->
    object : ResourceRegistry<GlShader>(
        BigShotApi.id("shaders/${shaderType.name.lowercase()}"),
        mutableListOf(),
        mutableListOf(FileToIdConverter("neo/shaders", shaderType.extension))
    ) {
        override fun onResourceManagerReload(manager: ResourceManager) {
            GlQueue.INSTANCE.runOrQueue {
                super.onResourceManagerReload(manager)
            }
        }

        override fun decode(
            location: Identifier,
            reader: BufferedReader,
            manager: ResourceManager
        ): DataResult<GlShader> {
            val shader = GlShader(location, shaderType)
            shader.source = NeoShaderLoader.preprocessors.fold(reader.readText().trim()) { code, preprocessor -> preprocessor.apply(location, code, manager) }

            if (shader.compile()) {
                return DataResult.success(shader)
            } else {
                val message = "Error compiling shader:\n${shader.getInfoLog()}"
                shader.close()
                return DataResult.error { message }
            }
        }
    }
}

object NeoShaderLoader : ResourceRegistry<GlProgram>(
    BigShotApi.id("shaders"),
    mutableListOf<ResourceRegistry<*>>(shaderIncludes).also { it.addAll(shaderRegistries.values) },
    mutableListOf(FileToIdConverter.json("neo/shaders"))
), BigShotClientEntrypoint {
    @JvmField
    val preprocessors = hashSetOf<ShaderPreprocessor>(ShaderIncludePreprocessor)
    override val modId: String = BigShotApi.MOD_ID

    override fun onInitializeClient(bus: NeoClientEventBus) {
        bus.register(AddAssetReloadListenersEvent {
            it(this)
        })
    }

    override fun decode(location: Identifier, reader: BufferedReader, manager: ResourceManager): DataResult<GlProgram> {
        val json = JsonParser.parseReader(reader).asJsonObject
        val formatKey = Identifier.parse(json.getAsJsonPrimitive("format").asString)
        val program = GlProgram(location, NeoVertexFormats.REGISTRY[formatKey] ?: return DataResult.error { "Nonexistent vertex format $formatKey" })
        val sources = json.getAsJsonObject("sources")

        for (entry in sources.asMap()) {
            val shaderKey = Identifier.parse(entry.value.asString)
            val shader = shaderRegistries[GlShaderType.valueOf(entry.key.uppercase())][shaderKey]

            if (shader == null) {
                program.close()
                return DataResult.error { "Unknown ${entry.key} shader $shaderKey" }
            }

            program.attach(shader)
        }

        if (program.link()) {
            return DataResult.success(program)
        } else {
            val message = "Error linking program:\n${program.getInfoLog()}"
            program.close()
            return DataResult.error { message }
        }
    }
}