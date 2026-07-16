package net.typho.big_shot_lib.client.api.rendering

import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.Resource
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuShaderType

interface NeoShaderPreprocessor {
    fun apply(location: Identifier, type: GpuShaderType, code: String, resources: Map<Identifier, Resource>): String

    companion object {
        @JvmField
        val REGISTRY = mutableSetOf<NeoShaderPreprocessor>(ShaderIncludePreprocessor)
    }
}