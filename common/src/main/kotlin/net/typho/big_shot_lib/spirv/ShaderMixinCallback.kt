package net.typho.big_shot_lib.spirv

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.ShaderType

interface ShaderMixinCallback {
    fun mixinPreCompile(shader: ResourceLocation, type: ShaderType, format: VertexFormat?, code: String): String = code

    fun mixinPostCompile(shader: ResourceLocation, type: ShaderType, format: VertexFormat?, context: ShaderMixinContext, locations: ShaderLocationsInfo) {
    }
}