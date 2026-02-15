package net.typho.big_shot_lib.api.client.rendering.shaders.mixins

import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceKey

object ShaderVersionUpdaterMixin : ShaderMixin {
    const val SPIRV_VERSION = 450
    const val SPIRV_VERSION_STRING = "$SPIRV_VERSION core"
    const val GLSL_VERSION = 150
    const val GLSL_VERSION_STRING = "$SPIRV_VERSION core"

    override fun mixinPreCompile(key: ShaderSourceKey, code: String): String {
        if (code.startsWith("#version")) {
            val version = code.split("\\s+".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()[1].toInt()

            if (version < SPIRV_VERSION) {
                return "#version $SPIRV_VERSION_STRING" + code.substring(code.indexOf('\n'))
            }
        } else {
            return "#version $SPIRV_VERSION_STRING\n$code"
        }

        return code
    }

    override fun mixinPostCompile(key: ShaderSourceKey, code: String): String {
        if (code.startsWith("#version")) {
            val version = code.split("\\s+".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()[1].toInt()

            if (version < GLSL_VERSION) {
                return "#version $GLSL_VERSION_STRING" + code.substring(code.indexOf('\n'))
            }
        } else {
            return "#version $GLSL_VERSION_STRING\n$code"
        }

        return code
    }
}