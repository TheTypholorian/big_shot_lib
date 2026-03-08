package net.typho.big_shot_lib.api.client.opengl.state.shards

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShader
import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class NeoShaderShard(
    @JvmField
    val location: ResourceIdentifier?,
    @JvmField
    val uniforms: (arguments: RenderArguments, shader: NeoShader) -> GlBindResult = { arguments, shader -> GlBindResult.Success }
) : ShaderShard({
    if (location == null) {
        null to GlBindResult.Success
    } else {
        val shader = NeoShaderRegistry.get(location)

        shader to if (shader == null) GlBindResult.Error("Could not find shader $location") else uniforms(it, shader)
    }
}) {
    companion object : RenderSettingShard.Type<NeoShaderShard> {
        override val default = NeoShaderShard(null)
        override val codec = null
        override val location = BigShotApi.id("shader")
    }
}