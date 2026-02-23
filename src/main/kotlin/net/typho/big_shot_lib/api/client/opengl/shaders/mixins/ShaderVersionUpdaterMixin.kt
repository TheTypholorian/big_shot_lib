package net.typho.big_shot_lib.api.client.opengl.shaders.mixins

import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceKey

object ShaderVersionUpdaterMixin : ShaderMixin {
    const val SPIRV_VERSION = 430
    const val SPIRV_VERSION_STRING = "$SPIRV_VERSION core"

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
}