package net.typho.big_shot_lib.impl.client.rendering.opengl

import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface ShaderInstanceExtension {
    fun `big_shot_lib$init`(
        location: NeoIdentifier,
        format: NeoVertexFormat,
        glId: Int
    )

    fun `big_shot_lib$initUniforms`()
}