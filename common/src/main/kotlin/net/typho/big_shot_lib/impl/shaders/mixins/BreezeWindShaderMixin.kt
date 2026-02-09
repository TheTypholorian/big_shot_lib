package net.typho.big_shot_lib.impl.shaders.mixins

import net.typho.big_shot_lib.api.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.util.BigShotModUtil.equals

object BreezeWindShaderMixin : ShaderMixin {
    override fun mixinPreCompile(key: ShaderSourceKey, code: String): String {
        if (key.program.location.equals("minecraft", "rendertype_breeze_wind")) {
            return code.replace("out vec4 lightMapColor;", "")
                .replace("lightMapColor =", "vec4 lightMapColor =")
        }

        return code
    }
}