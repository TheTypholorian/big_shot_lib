package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.error.ShaderCompileException
import net.typho.big_shot_lib.api.util.resource.NamedResource

interface GlShader : NamedResource, GlResource {
    val shaderType: GlShaderType
    var source: String

    fun getInfoLog(): String

    fun compile(): Boolean

    fun compileOrThrow(onError: (log: String) -> Unit = { throw ShaderCompileException("Error compiling shader $location:\n$it") }) {
        if (!compile()) {
            onError(getInfoLog())
        }
    }
}