package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.error.ShaderCompileException
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

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

    companion object {
        @JvmStatic
        @JvmOverloads
        fun create(location: NeoIdentifier, type: GlShaderType, glId: Int = type.resourceType.create()): GlShader = InternalUtil.INSTANCE.createShader(
            location,
            type,
            glId,
        )
    }
}