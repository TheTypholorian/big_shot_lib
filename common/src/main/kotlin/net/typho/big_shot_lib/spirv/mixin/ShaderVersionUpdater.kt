package net.typho.big_shot_lib.spirv.mixin

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.ShaderType
import net.typho.big_shot_lib.spirv.ShaderMixinCallback

object ShaderVersionUpdater : ShaderMixinCallback {
    override fun mixinGLSL(shader: ResourceLocation, type: ShaderType, format: VertexFormat?, code: String): String {
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