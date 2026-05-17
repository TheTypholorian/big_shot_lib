package net.typho.big_shot_lib.api.client.rendering

import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.api.util.resource.NamedResource

interface ShaderPreprocessor : NamedResource {
    fun apply(location: Identifier, code: String, manager: ResourceManager): String
}