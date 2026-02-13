package net.typho.big_shot_lib.api.client.rendering.shaders.mixins

import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceKey

object ShaderVersionUpdaterMixin : ShaderMixin {
    const val VERSION = 450
    const val VERSION_STRING = "$VERSION core"

    override fun mixinPreCompile(key: ShaderSourceKey, code: String): String {
        if (code.startsWith("#version")) {
            val version = code.split("\\s+".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()[1].toInt()

            if (version < VERSION) {
                return "#version $VERSION_STRING" + code.substring(code.indexOf('\n'))
            }
        } else {
            return "#version $VERSION_STRING\n$code"
        }

        return code
    }
}