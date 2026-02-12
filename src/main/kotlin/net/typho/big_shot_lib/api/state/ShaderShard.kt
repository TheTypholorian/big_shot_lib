package net.typho.big_shot_lib.api.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.shaders.GlShader
import net.typho.big_shot_lib.api.shaders.GlShaderRegistry

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

        override fun location(): Identifier = Identifier.fromNamespaceAndPath("opengl", "shader")
    }
}