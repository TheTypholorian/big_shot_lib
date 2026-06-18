package net.typho.big_shot_lib.api.client.rendering

import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager

interface ShaderPreprocessor {
    fun apply(location: Identifier, code: String, manager: ResourceManager): String
}