package net.typho.big_shot_lib.spirv.mixin

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.ShaderType

object BreezeWindShaderFix : ShaderMixinCallback {
    override fun mixinGLSL(shader: ResourceLocation, type: ShaderType, format: VertexFormat?, code: String): String {
        if (shader == ResourceLocation.withDefaultNamespace("rendertype_breeze_wind")) {
            return code.replace("out vec4 lightMapColor;", "")
                .replace("lightMapColor =", "vec4 lightMapColor =")
        }

        return code
    }
}