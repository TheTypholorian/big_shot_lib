package net.typho.big_shot_lib.api.client.rendering

import com.google.gson.JsonParser
import com.mojang.serialization.DataResult
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManager
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.client.util.resource.ResourceRegistry
import net.typho.big_shot_lib.api.util.*
import net.typho.big_shot_lib.api.util.resource.NeoFileToIdConverter
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey.Companion.lookupOrThrow
import java.io.BufferedReader

@JvmField
val shaderIncludes = object : ResourceRegistry<String>(
    BigShotApi.id("shaders/include"),
    mutableListOf(),
    mutableListOf(
        NeoFileToIdConverter("neo/shaders/include", "glsl"),
        NeoFileToIdConverter("shaders/include", "glsl")
    )
) {
    override fun decode(
        location: NeoIdentifier,
        reader: BufferedReader,
        manager: NeoResourceManager
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
        mutableListOf(NeoFileToIdConverter.shader("neo/shaders", shaderType))
    ) {
        override fun onResourceManagerReload(manager: NeoResourceManager) {
            GlQueue.INSTANCE.runOrQueue {
                super.onResourceManagerReload(manager)
            }
        }

        override fun decode(
            location: NeoIdentifier,
            reader: BufferedReader,
            manager: NeoResourceManager
        ): DataResult<GlShader> {
            val shader = GlShader.create(location, shaderType)
            shader.source = NeoShaderLoader.CommonInit.preprocessors.lookupOrThrow().values().fold(reader.readText().trim()) { code, preprocessor -> preprocessor.apply(location, code, manager) }

            if (shader.compile()) {
                return DataResult.success(shader)
            } else {
                val message = "Error compiling shader:\n${shader.getInfoLog()}"
                shader.free()
                return DataResult.error { message }
            }
        }
    }
}

object NeoShaderLoader : ResourceRegistry<GlProgram>(
    BigShotApi.id("shaders"),
    mutableListOf<ResourceRegistry<*>>(shaderIncludes).also { it.addAll(shaderRegistries.values) },
    mutableListOf(NeoFileToIdConverter.json("neo/shaders"))
) {
    // TODO unfuck this
    object ClientInit : BigShotClientEntrypoint(BigShotApi.MOD_ID) {
        override fun onInitializeClient() {
        }

        override fun addReloadListeners(out: (listener: NeoResourceManagerReloadListener) -> Unit) {
            out(NeoShaderLoader)
        }
    }

    object CommonInit : BigShotCommonEntrypoint(BigShotApi.MOD_ID) {
        @JvmField
        val preprocessors = createRegistry<ShaderPreprocessor>(BigShotApi.id("shader_preprocessors"))
        val shaderIncludePreprocessor by register(preprocessors, ShaderIncludePreprocessor)

        override fun onInitialize() {
        }
    }

    override fun decode(location: NeoIdentifier, reader: BufferedReader, manager: NeoResourceManager): DataResult<GlProgram> {
        val json = JsonParser.parseReader(reader).asJsonObject
        val formatKey = NeoIdentifier(json.getAsJsonPrimitive("format").asString)
        val program = GlProgram.create(location, NeoVertexFormat.REGISTRY.lookupOrThrow().get(formatKey) ?: return DataResult.error { "Nonexistent vertex format $formatKey" })
        val sources = json.getAsJsonObject("sources")

        for (entry in sources.asMap()) {
            val shaderKey = NeoIdentifier(entry.value.asString)
            val shader = shaderRegistries[GlShaderType.valueOf(entry.key.uppercase())][shaderKey]

            if (shader == null) {
                program.free()
                return DataResult.error { "Unknown ${entry.key} shader $shaderKey" }
            }

            program.attach(shader)
        }

        if (program.link()) {
            return DataResult.success(program)
        } else {
            val message = "Error linking program:\n${program.getInfoLog()}"
            program.free()
            return DataResult.error { message }
        }
    }
}