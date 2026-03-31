package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.error.ShaderLinkException
import net.typho.big_shot_lib.api.error.ShaderValidationException
import net.typho.big_shot_lib.api.util.resource.NamedResource

interface GlProgram : NamedResource, GlResource {
    val format: NeoVertexFormat

    fun use(): GlBoundProgram

    fun attach(shader: GlShader)

    fun detach(shader: GlShader)

    fun link(onError: (log: String) -> Nothing = { throw ShaderLinkException("Error linking program $location:\n$it") })

    fun validate(onError: (log: String) -> Nothing = { throw ShaderValidationException("Invalid program $location:\n$it") })
}