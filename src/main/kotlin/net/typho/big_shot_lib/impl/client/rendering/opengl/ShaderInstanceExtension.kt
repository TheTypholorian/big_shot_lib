package net.typho.big_shot_lib.impl.client.rendering.opengl

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat

interface ShaderInstanceExtension {
    fun `big_shot_lib$init`(
        location: Identifier,
        format: NeoVertexFormat,
        glId: Int
    )

    fun `big_shot_lib$initUniforms`()
}