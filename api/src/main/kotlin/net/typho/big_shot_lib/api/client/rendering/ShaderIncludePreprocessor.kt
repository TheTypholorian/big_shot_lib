package net.typho.big_shot_lib.api.client.rendering

import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.Resource
import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuShaderType

object ShaderIncludePreprocessor : NeoShaderPreprocessor {
    @JvmField
    val directories = mutableSetOf(
        FileToIdConverter("neo/shaders/include", ".glsl"),
        FileToIdConverter("shaders/include", ".glsl")
    )

    override fun apply(
        location: Identifier,
        type: GpuShaderType,
        code: String,
        resources: Map<Identifier, Resource>
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
                BigShotLib.LOGGER.error("Malformed #include '$line' in shader $location")
                break
            }

            var contents = line[1]

            if (
                (contents.startsWith('"') && contents.endsWith('"')) ||
                (contents.startsWith('<') && contents.endsWith('>'))
            ) {
                contents = contents.substring(1, contents.length - 1)
            }

            val includePath = Identifier.parse(contents)
            var found = false

            for (dir in directories) {
                resources[dir.idToFile(includePath)]?.let { resource ->
                    resource.openAsReader().use { reader ->
                        var text = reader.readText().trim()

                        if (text.startsWith("#version")) {
                            text = text.substring(text.indexOf('\n') + 1)
                        }

                        code = code.substring(0, index) + text + code.substring(endIndex)
                    }

                    found = true
                    break
                }
            }

            if (!found) {
                BigShotLib.LOGGER.error("Could not find include file '$includePath' requested by shader $location")
                break
            }
        }

        return code
    }
}