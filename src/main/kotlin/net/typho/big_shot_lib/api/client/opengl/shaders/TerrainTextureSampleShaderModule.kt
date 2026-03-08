package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArgumentType
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult

/**
 * A utility method for sampling the block atlas when rendering terrain, includes RGSS sampling.
 *
 * Defines methods:
 * ```glsl
 * vec4 bigShotTerrainTexture(vec2)
 * ```
 */
interface TerrainTextureSampleShaderModule : ShaderModule {
    override fun setUniforms(arguments: RenderArguments, shader: GlShader): GlBindResult {
        val atlas = arguments.get(RenderArgumentType.BLOCK_ATLAS) { return it }
        shader.getUniform("BlockAtlasSampler")?.setSampler(atlas)
        shader.getUniform("BlockAtlasSize")?.setValue(atlas.width, atlas.height)
        return GlBindResult.Success
    }

    override fun loadGLSL(files: ShaderFileResolver, key: ShaderProgramKey, source: ShaderSourceType): String? {
        return files.loadFile(type.location.withSuffix(".glsl"))
    }

    companion object : ShaderModule.ServiceType<TerrainTextureSampleShaderModule>(TerrainTextureSampleShaderModule::class) {
        override val dependencies: Set<Pair<ShaderModule.Type<*>, () -> ShaderModule?>> = setOf()
        override val location = BigShotApi.id("terrain_texture_sample")
    }
}