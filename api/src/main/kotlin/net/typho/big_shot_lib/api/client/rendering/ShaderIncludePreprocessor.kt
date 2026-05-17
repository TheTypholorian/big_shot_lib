package net.typho.big_shot_lib.api.client.rendering

import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.api.BigShotApi
import java.io.FileNotFoundException

object ShaderIncludePreprocessor : ShaderPreprocessor {
    override val location: Identifier = BigShotApi.id("shader_includes")

    override fun apply(
        location: Identifier,
        code: String,
        manager: ResourceManager
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

            val includePath = Identifier.parse(contents)

            code = code.substring(0, index) +
                    (shaderIncludes[includePath] ?: throw FileNotFoundException("Could not find include file '$includePath' requested by $location")) +
                    code.substring(endIndex)
        }

        return code
    }
}