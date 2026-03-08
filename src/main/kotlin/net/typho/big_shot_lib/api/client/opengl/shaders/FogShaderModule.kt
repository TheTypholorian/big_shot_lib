package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.BigShotApi

/**
 * Defines methods:
 * ```glsl
 * vec4 bigShotFog(vec4 inColor, vec3 delta, vec4 fogColor)
 * vec4 bigShotFog(vec4 inColor, vec3 delta)
 * ```
 */
interface FogShaderModule : ShaderModule {
    override fun loadGLSL(files: ShaderFileResolver, key: ShaderProgramKey, source: ShaderSourceType): String? {
        return files.loadFile(type.location.withSuffix(".glsl"))
    }

    companion object : ShaderModule.ServiceType<FogShaderModule>(FogShaderModule::class) {
        override val dependencies: Set<Pair<ShaderModule.Type<*>, () -> ShaderModule?>> = setOf()
        override val location = BigShotApi.id("fog")
    }
}