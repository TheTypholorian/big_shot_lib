package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.error.ShaderLinkException
import net.typho.big_shot_lib.api.error.ShaderValidationException
import net.typho.big_shot_lib.api.util.resource.NamedResource
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface GlProgram : NamedResource, GlResource {
    val format: NeoVertexFormat

    fun use(): GlBoundProgram

    fun attach(shader: GlShader)

    fun detach(shader: GlShader)

    fun getInfoLog(): String

    fun link(): Boolean

    fun validate(): Boolean

    fun linkOrThrow(onError: (log: String) -> Unit = { throw ShaderLinkException("Error linking program $location:\n$it") }) {
        if (!link()) {
            onError(getInfoLog())
        }
    }

    fun validateOrThrow(onError: (log: String) -> Unit = { throw ShaderValidationException("Invalid program $location:\n$it") }) {
        if (!validate()) {
            onError(getInfoLog())
        }
    }
}