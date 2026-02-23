package net.typho.big_shot_lib.api.util.resources

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType

@JvmRecord
data class NeoFileToIdConverter(
    @JvmField
    val prefix: String,
    @JvmField
    val extension: String
) {
    fun idToFile(id: ResourceIdentifier): ResourceIdentifier {
        return id.withPrefix(prefix + PATH_DELIMITER).withSuffix(EXTENSION_DELIMITER + extension)
    }

    fun fileToId(file: ResourceIdentifier): ResourceIdentifier {
        return file.withPath { it.substring(prefix.length + 1, it.length - extension.length - 1) }
    }

    fun listMatchingResources(manager: NeoResourceManager): MutableMap<ResourceIdentifier, Resource> {
        return manager.listResources(prefix) { it.path.endsWith(extension) }
    }

    fun listMatchingResourceStacks(manager: ResourceManager): MutableMap<ResourceLocation, MutableList<Resource>> {
        return manager.listResourceStacks(prefix) { it.path.endsWith(extension) }
    }

    companion object {
        const val PATH_DELIMITER = '/'
        const val EXTENSION_DELIMITER = '.'

        @JvmStatic
        fun json(prefix: String) = NeoFileToIdConverter(prefix, "json")

        @JvmStatic
        fun shader(prefix: String, shader: ShaderSourceType) = NeoFileToIdConverter(prefix, shader.extension)
    }
}