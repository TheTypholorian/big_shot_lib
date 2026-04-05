package net.typho.big_shot_lib.api.client.rendering

import com.google.gson.JsonElement
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.ResourceListenerFactory
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManager
import net.typho.big_shot_lib.api.client.util.resource.ResourceRegistry
import net.typho.big_shot_lib.api.error.ShaderLinkException
import net.typho.big_shot_lib.api.util.*
import net.typho.big_shot_lib.api.util.RegistrationConsumer.Companion.register
import net.typho.big_shot_lib.api.util.resource.NeoFileToIdConverter
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import java.io.BufferedReader

object NeoShaderLoader : ResourceRegistry.Json<GlProgram?>(BigShotApi.id("shaders"), NeoFileToIdConverter.json("neo/shaders")), BigShotClientEntrypoint, BigShotCommonEntrypoint {
    @JvmField
    val PREPROCESSORS_REGISTRY_KEY = NeoResourceKey.registry<ShaderPreprocessor>(BigShotApi.id("shader_preprocessors"))
    var PREPROCESSORS_REGISTRY: NeoRegistry<ShaderPreprocessor>? = null
        private set

    @JvmField
    val includes = object : ResourceRegistry<String>(
        BigShotApi.id("shaders/include"),
        NeoFileToIdConverter("neo/shaders", "glsl")
    ) {
        override fun decode(
            location: NeoIdentifier,
            reader: BufferedReader,
            manager: NeoResourceManager
        ): String {
            return reader.readText()
        }
    }

    @JvmField
    val shaderRegistries = enumArrayMapOf<GlShaderType, ResourceRegistry<GlShader>> { shaderType ->
        object : ResourceRegistry<GlShader>(
            BigShotApi.id("shaders/${shaderType.name.lowercase()}"),
            NeoFileToIdConverter.shader("neo/shaders", shaderType)
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
            ): GlShader {
                val shader = NeoGlShader(location, shaderType)
                shader.source = PREPROCESSORS_REGISTRY!!.values().fold(reader.readText().trim()) { code, preprocessor -> preprocessor.apply(location, code, manager) }
                shader.compile()
                return shader
            }
        }
    }

    override fun registerReloadListeners(factory: ResourceListenerFactory) {
        factory.register(includes)
        shaderRegistries.forEach { (type, registry) -> factory.register(registry) }
        factory.register(this)
    }

    override fun registerRegistries(factory: RegistryFactory) {
        PREPROCESSORS_REGISTRY = factory.create(PREPROCESSORS_REGISTRY_KEY)
    }

    override fun registerContent(factory: RegistrationFactory) {
        factory.begin(PREPROCESSORS_REGISTRY_KEY)?.run {
            register(ShaderIncludePreprocessor)
        }
    }

    override fun decode(location: NeoIdentifier, json: JsonElement, manager: NeoResourceManager): GlProgram? {
        val json = json.asJsonObject
        val formatKey = NeoIdentifier(json.getAsJsonPrimitive("format").asString)
        val program = NeoGlProgram(location, NeoVertexFormat.REGISTRY!!.get(formatKey) ?: throw NullPointerException("Nonexistent vertex format $formatKey, requested by shader $location"))
        val sources = json.getAsJsonObject("sources")

        for (entry in sources.asMap()) {
            program.attach(shaderRegistries[GlShaderType.valueOf(entry.key.uppercase())][NeoIdentifier(entry.value.asString)]!!)
        }

        if (program.link()) {
            return program
        } else {
            ShaderLinkException("Error linking program $location:\n${program.getInfoLog()}").printStackTrace()
            return null
        }
    }
}