package net.typho.big_shot_lib.api.impl

import com.mojang.blaze3d.shaders.AbstractUniform
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.EffectInstance
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.IShader
import net.typho.big_shot_lib.gl.Unbindable
import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.spirv.ShaderExtension.Companion.getLocations

class BuiltinEffectShader(val inner: EffectInstance) : IShader {
    override fun getUniform(name: String): AbstractUniform? = inner.getUniform(name)

    override fun setSampler(name: String, id: Int) = inner.setSampler(name) { id }

    override fun vertexFormat(): VertexFormat? = DefaultVertexFormat.BLIT_SCREEN

    override fun bind(): Unbindable<BuiltinEffectShader> {
        inner.apply()
        return Unbindable.of(this)
    }

    override fun release() {
        inner.close()
    }

    override fun location(): ResourceLocation = ResourceLocation.withDefaultNamespace(inner.name)

    override fun type() = GlResourceType.PROGRAM

    override fun id() = inner.id

    override fun `big_shot_lib$getLocations`() = inner.getLocations()
}