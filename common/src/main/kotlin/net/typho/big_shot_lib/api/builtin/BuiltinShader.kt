package net.typho.big_shot_lib.api.builtin

import com.mojang.blaze3d.shaders.AbstractUniform
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.CompiledShaderProgram
import net.minecraft.client.renderer.ShaderProgram
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.IShader
import net.typho.big_shot_lib.gl.resource.ExtraUnbind
import net.typho.big_shot_lib.gl.resource.GlResourceType

class BuiltinShader(
    val config: ShaderProgram,
    val compiled: CompiledShaderProgram = Minecraft.getInstance().shaderManager.getProgram(config)!!
) : IShader, ExtraUnbind {
    override fun getUniform(name: String): AbstractUniform? = compiled.getUniform(name)

    override fun setSampler(name: String, id: Int) = compiled.bindSampler(name, id)

    override fun vertexFormat(): VertexFormat? = config.vertexFormat

    override fun bind() {
        compiled.apply()
    }

    override fun unbind() {
        unbindExtra()
    }

    override fun unbindExtra() {
        compiled.clear()
    }

    override fun release() {
        compiled.close()
    }

    override fun location(): ResourceLocation = config.configId

    override fun type() = GlResourceType.PROGRAM

    override fun id() = compiled.programId
}