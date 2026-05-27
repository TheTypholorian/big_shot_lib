package net.typho.big_shot_lib.impl.client.rendering.opengl

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.Identifier

interface ShaderInstanceExtension {
    fun `big_shot_lib$init`(
        location: Identifier,
        format: VertexFormat,
        glId: Int
    )

    fun `big_shot_lib$initUniforms`()
}