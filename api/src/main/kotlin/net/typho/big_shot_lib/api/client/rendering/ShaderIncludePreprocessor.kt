package net.typho.big_shot_lib.api.client.rendering

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManager
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import java.io.FileNotFoundException

object ShaderIncludePreprocessor : ShaderPreprocessor {
    override val location: NeoIdentifier = BigShotApi.id("shader_includes")

    override fun apply(
        location: NeoIdentifier,
        code: String,
        manager: NeoResourceManager
    ): String {
        var code = code
        var index: Int = -1

        fun hasIncludes(): Boolean {
            index = code.indexOf("\n#include")

            if (index == -1) {
                if (code.startsWith("#include")) {
                    index = 0
                    return true
                }
            } else {
                index++
            }

            return index != -1
        }

        while (hasIncludes()) {
            var endIndex = code.indexOf('\n', index)

            if (endIndex == -1) {
                endIndex = code.length
            }

            val line = code.substring(index, endIndex)
                .trim()
                .split(Regex("\\s+"))
                .mapNotNull { it.trim().ifEmpty { null } }

            if (line.size != 2) {
                throw IllegalStateException("Malformed #include '$line'")
            }

            var contents = line[1]

            if (
                (contents.startsWith('"') && contents.endsWith('"')) ||
                (contents.startsWith('<') && contents.endsWith('>'))
            ) {
                contents = contents.substring(1, contents.length - 1)
            }

            val includePath = NeoIdentifier(contents)

            code = code.substring(0, index) +
                    (NeoShaderLoader.includes[includePath] ?: throw FileNotFoundException("Could not find include file '$includePath' requested by $location"))
                    code.substring(endIndex)
        }

        return code
    }
}