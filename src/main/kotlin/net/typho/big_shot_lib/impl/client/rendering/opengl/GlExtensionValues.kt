package net.typho.big_shot_lib.impl.client.rendering.opengl

import com.mojang.blaze3d.shaders.Program
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.renderer.ShaderInstance
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.util.ImmutableExtension

// TODO
interface GlProgramExtensionValue : GlProgram, ImmutableExtension<ShaderInstance>

interface GlShaderExtensionValue : GlShader, ImmutableExtension<Program>
