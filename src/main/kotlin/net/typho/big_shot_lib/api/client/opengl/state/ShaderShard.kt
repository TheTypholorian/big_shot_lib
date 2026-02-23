package net.typho.big_shot_lib.api.client.opengl.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.shaders.NeoShader
import net.typho.big_shot_lib.api.client.rendering.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class ShaderShard(
    @JvmField
    val shader: NeoShader
) : RenderSettingShard.Basic(
    ShaderShard,
    listOf(shader)
) {
    companion object : RenderSettingShard.Type<ShaderShard> {
        override fun getDefault() = ShaderShard(NeoShader.NULL)

        override fun codec(): MapCodec<ShaderShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoShaderRegistry.lookupCodec.fieldOf("shader").forGetter { shard -> shard.shader }
            ).apply(it, ::ShaderShard)
        }

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "shader")
    }
}