package net.typho.big_shot_lib.api.shaders

import net.minecraft.server.packs.resources.Resource
import net.typho.big_shot_lib.api.errors.ResourceNotFoundException
import net.typho.big_shot_lib.api.services.ResourceManagerWrapper
import net.typho.big_shot_lib.api.util.ResourceIdentifier
import java.io.InputStream

fun interface ShaderFileResolver {
    fun loadFile(key: String, requester: String): String?

    fun loadIncludes(code: String, requester: String): String {
        var modified = code
        var i = 0

        while (i < modified.length) {
            for (key in includeKeys) {
                if (modified.startsWith(key, i)) {
                    val start = i + key.length
                    val end = modified.indexOf('\n', start)
                    val code = loadFile(modified.substring(start, end).trim().trim('"', '<', '>'), key)
                    modified = modified.substring(0, i) + code + modified.substring(end)
                    i = 0
                    break
                }
            }

            i++
        }

        return modified
    }

    companion object {
        @JvmField
        val includeKeys: MutableList<String> = mutableListOf("#include", "#moj_import")
        @JvmField
        val directories: MutableList<String> = mutableListOf("", "neo/shaders/", "shaders/include/")

        @JvmStatic
        fun stripVersion(code: String): String {
            var modified = code

            while (modified.contains("#version")) {
                val index = modified.indexOf("#version")
                modified = modified.substring(0, index) + modified.substring(modified.indexOf('\n', index))
            }

            return modified
        }
    }

    open class ResourceBacked(
        @JvmField
        val manager: ResourceManagerWrapper
    ) : ShaderFileResolver {
        override fun loadFile(key: String, requester: String): String? {
            val id = ResourceIdentifier(key).withPath { path ->
                if (path.contains('.')) {
                    return@withPath path
                } else {
                    return@withPath "$path.glsl"
                }
            }

            var resource: Resource? = null

            for (dir in directories) {
                val found = manager.getResource(id.withPrefix(dir))

                if (found.isPresent) {
                    resource = found.get()
                    break
                }
            }

            if (resource == null) {
                throw ResourceNotFoundException("Couldn't find shader file $id, requested by ${requester}. Searched in $directories")
            }

            return stripVersion(String(resource.open().use(InputStream::readAllBytes)))
        }
    }
}