package net.typho.big_shot_lib.impl.shaders.mixins

import net.typho.big_shot_lib.api.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixin

object ShaderVersionUpdaterMixin : ShaderMixin {
    override fun mixinPreCompile(key: ShaderSourceKey, code: String): String {
        if (code.startsWith("#version")) {
            val version =
                code.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()

            if (version < 450) {
                return "#version 450 core" + code.substring(code.indexOf('\n'))
            }
        } else {
            return "#version 450 core\n$code"
        }

        return code
    }
}