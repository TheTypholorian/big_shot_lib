package net.typho.big_shot_lib.api.client.opengl.state.shards

import net.typho.big_shot_lib.api.client.opengl.shaders.GlShader
import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShader
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class ShaderShard(
    @JvmField
    val shader: (arguments: RenderArguments) -> Pair<GlShader?, GlBindResult>
) : RenderSettingShard {
    override val type: RenderSettingShard.Type<ShaderShard> = ShaderShard

    override fun bind(arguments: RenderArguments, pushStack: Boolean): GlBindResult {
        val shader = shader(arguments)

        if (!shader.second.success) {
            return shader.second
        }

        shader.first?.bind(pushStack) ?: NeoShader.NULL.bind(pushStack)

        return shader.second
    }

    override fun unbind(popStack: Boolean) {
        NeoShader.NULL.unbind(popStack)
    }

    companion object : RenderSettingShard.Type<ShaderShard> {
        override val default = ShaderShard { null to GlBindResult.Success }
        override val codec = null
        /*
        : MapCodec<ShaderShard> = RecordCodecBuilder.mapCodec {
            it.group(
                ResourceIdentifier.CODEC.optionalFieldOf("shader").forGetter { shard -> Optional.ofNullable(shard.shader) }
            ).apply(it) { id -> ShaderShard(id.getOrNull()) }
        }
         */
        override val location = ResourceIdentifier("opengl", "shader")
    }
}