package net.typho.big_shot_lib.impl.shaders.mixins

import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixin

object BreezeWindShaderMixin : ShaderMixin {
    override fun mixinPreCompile(key: ShaderSourceKey, code: String): String {
        if (key.program.location.equals("minecraft", "rendertype_breeze_wind")) {
            return code.replace("out vec4 lightMapColor;", "")
                .replace("lightMapColor =", "vec4 lightMapColor =")
        }

        return code
    }
}