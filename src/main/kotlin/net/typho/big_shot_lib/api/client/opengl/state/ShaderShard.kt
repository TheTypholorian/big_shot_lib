package net.typho.big_shot_lib.api.client.opengl.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.*
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrNull

open class ShaderShard(
    @JvmField
    val shader: ResourceIdentifier?
) : RenderSettingShard.Basic(
    ShaderShard,
    if (shader == null)
        listOf()
    else
        listOf(GlBindable.ofStack(GlStateStack.shader, Supplier { NeoShaderRegistry.get(shader)!!.glId }))
) {
    companion object : RenderSettingShard.Type<ShaderShard> {
        override fun getDefault() = ShaderShard(null)

        override fun codec(): MapCodec<ShaderShard> = RecordCodecBuilder.mapCodec {
            it.group(
                ResourceIdentifier.CODEC.optionalFieldOf("shader").forGetter { shard -> Optional.ofNullable(shard.shader) }
            ).apply(it) { id -> ShaderShard(id.getOrNull()) }
        }

        override val location = ResourceIdentifier("opengl", "shader")
    }
}