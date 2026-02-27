package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShader
import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.function.Consumer

open class ShaderShard(
    @JvmField
    val shader: ResourceIdentifier?,
    @JvmField
    val uniforms: Consumer<NeoShader>
) : RenderSettingShard {
    override val type = ShaderShard

    override fun bind(pushStack: Boolean) {
        if (shader != null) {
            val shader = NeoShaderRegistry.get(shader)!!
            shader.bind(pushStack)
            uniforms.accept(shader)
        }
    }

    override fun unbind(popStack: Boolean) {
        if (shader != null) {
            NeoShaderRegistry.get(shader)!!.unbind(popStack)
        }
    }

    companion object : RenderSettingShard.Type<ShaderShard> {
        override val default = ShaderShard(null) { }
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