package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArgumentType
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult

/**
 * Defines uniforms:
 * ```glsl
 * mat4 IProjMat;
 * mat4 IModelViewMat;
 * ```
 */
object InverseMatricesShaderModule : ShaderModule.UnitType<InverseMatricesShaderModule>() {
    override val dependencies: Set<Pair<ShaderModule.Type<*>, () -> ShaderModule?>> = setOf()
    override val location = BigShotApi.id("inverse_vertex_matrices")

    override fun setUniforms(arguments: RenderArguments, shader: GlShader): GlBindResult {
        val proj = arguments.get(RenderArgumentType.INVERSE_PROJ_MAT) { return it }
        val modelView = arguments.get(RenderArgumentType.INVERSE_MODEL_MAT) { return it }

        shader.getUniform("IProjMat")?.setValue(proj)
        shader.getUniform("IModelViewMat")?.setValue(modelView)

        return GlBindResult.Success
    }

    override fun loadGLSL(files: ShaderFileResolver, key: ShaderProgramKey, source: ShaderSourceType): String {
        return "uniform mat4 IProjMat;\nuniform mat4 IModelViewMat;"
    }
}