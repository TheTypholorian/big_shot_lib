package net.typho.big_shot_lib.api.client.rendering.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.shaders.GlShader
import net.typho.big_shot_lib.api.client.rendering.shaders.GlShaderRegistry
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class ShaderShard(
    @JvmField
    val shader: GlShader
) : RenderSettingShard.Basic(
    ShaderShard,
    listOf(shader)
) {
    companion object : RenderSettingShard.Type<ShaderShard> {
        override fun getDefault() = ShaderShard(GlShader.NULL)

        override fun codec(): MapCodec<ShaderShard> = RecordCodecBuilder.mapCodec {
            it.group(
                GlShaderRegistry.lookupCodec.fieldOf("shader").forGetter { shard -> shard.shader }
            ).apply(it, ::ShaderShard)
        }

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "shader")
    }
}