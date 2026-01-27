package net.typho.big_shot_lib.shaders

import com.mojang.blaze3d.shaders.AbstractUniform
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.ExtraUnbind
import net.typho.big_shot_lib.gl.resource.GlResourceType

class BuiltinShader(val inner: ShaderInstance) : IShader, ExtraUnbind {
    override fun getUniform(name: String): AbstractUniform? = inner.getUniform(name)

    override fun setSampler(name: String, id: Int) = inner.setSampler(name, id)

    override fun vertexFormat(): VertexFormat? = inner.vertexFormat

    override fun bind() {
        inner.apply()
    }

    override fun unbind() {
        unbindExtra()
    }

    override fun unbindExtra() {
        inner.clear()
    }

    override fun release() {
        inner.close()
    }

    override fun location(): ResourceLocation = ResourceLocation.withDefaultNamespace(inner.name)

    override fun type() = GlResourceType.PROGRAM

    override fun id() = inner.id
}