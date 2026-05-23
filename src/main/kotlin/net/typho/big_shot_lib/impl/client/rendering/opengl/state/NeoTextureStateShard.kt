package net.typho.big_shot_lib.impl.client.rendering.opengl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding
import java.util.Optional

class NeoTextureStateShard(
    @JvmField
    val binding: GlTextureBinding
) : RenderStateShard.EmptyTextureStateShard(
    {
        val texture = binding.texture
        // TODO set filters
        RenderSystem.setShaderTexture(0, texture.glId)
    },
    {
    }
) {
    override fun cutoutTexture(): Optional<Identifier> {
        return Optional.ofNullable(binding.location)
    }
}