package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.minecraft.client.renderer.ShaderInstance
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.error.ShaderLinkException
import net.typho.big_shot_lib.api.error.ShaderValidationException
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.impl.util.ImmutableExtension

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

    companion object {
        @JvmStatic
        @JvmOverloads
        fun create(location: NeoIdentifier, format: NeoVertexFormat, glId: Int = GlResourceType.PROGRAM.create()): GlProgram = InternalUtil.INSTANCE.createProgram(
            location,
            format,
            glId,
        )
    }

    interface ExtensionValue : GlProgram, ImmutableExtension<ShaderInstance>
}