package net.typho.big_shot_lib.api.client.opengl.shaders

import net.minecraft.server.packs.resources.Resource
import net.typho.big_shot_lib.api.util.resources.NeoIdentifier
import net.typho.big_shot_lib.api.util.resources.NeoResourceManager
import java.io.InputStream

interface ShaderFileResolver {
    fun loadFile(key: String): String? {
        return loadFile(
            NeoIdentifier(key).withPath { path ->
                if (path.contains('.')) {
                    return@withPath path
                } else {
                    return@withPath "$path.glsl"
                }
            }
        )
    }

    fun loadFile(location: NeoIdentifier, source: ShaderSourceType): String? {
        return loadFile(location.withSuffix(".${source.extension}"))
    }

    fun loadFile(location: NeoIdentifier): String?

    /*
    fun loadIncludes(code: String): String {
        var modified = code
        var i = 0

        while (i < modified.length) {
            for (key in includeKeys) {
                if (modified.startsWith(key, i)) {
                    val start = i + key.length
                    val end = modified.indexOf('\n', start)
                    val code = loadFile(modified.substring(start, end).trim().trim('"', '<', '>'))
                    modified = modified.substring(0, i) + code + modified.substring(end)
                    i = 0
                    break
                }
            }

            i++
        }

        return modified
    }
     */

    companion object {
        //@JvmField
        //val includeKeys: MutableList<String> = mutableListOf("#include", "#moj_import", "#import")
        @JvmField
        val directories: MutableList<String> = mutableListOf("", "neo/shaders/", "shaders/include/")
    }

    open class ResourceBacked(
        @JvmField
        val manager: NeoResourceManager
    ) : ShaderFileResolver {
        override fun loadFile(location: NeoIdentifier): String? {
            var resource: Resource? = null

            for (dir in directories) {
                val found = manager.getResource(location.withPrefix(dir))

                if (found.isPresent) {
                    resource = found.get()
                    break
                }
            }

            return resource?.let { String(it.open().use(InputStream::readAllBytes)) }
        }
    }
}