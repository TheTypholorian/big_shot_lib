package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArgumentType
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult

/**
 * Defines uniforms:
 * ```glsl
 * mat4 ProjMat;
 * mat4 ModelViewMat;
 * ```
 */
object VertexMatricesShaderModule : ShaderModule.UnitType<VertexMatricesShaderModule>() {
    override val dependencies: Set<Pair<ShaderModule.Type<*>, () -> ShaderModule?>> = setOf()
    override val location = BigShotApi.id("vertex_matrices")

    override fun setUniforms(arguments: RenderArguments, shader: GlShader): GlBindResult {
        val proj = arguments.get(RenderArgumentType.PROJ_MAT) { return it }
        val modelView = arguments.get(RenderArgumentType.MODEL_MAT) { return it }

        shader.getUniform("ProjMat")?.setValue(proj)
        shader.getUniform("ModelViewMat")?.setValue(modelView)

        return GlBindResult.Success
    }

    override fun loadGLSL(files: ShaderFileResolver, key: ShaderProgramKey, source: ShaderSourceType): String? {
        return if (source == ShaderSourceType.VERTEX || source == ShaderSourceType.FRAGMENT) "uniform mat4 ProjMat;\nuniform mat4 ModelViewMat;" else null
    }
}