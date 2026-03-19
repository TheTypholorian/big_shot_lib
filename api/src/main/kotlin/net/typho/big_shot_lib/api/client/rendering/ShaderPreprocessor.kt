package net.typho.big_shot_lib.api.client.rendering

import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManager
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface ShaderPreprocessor : NamedResource {
    fun apply(location: NeoIdentifier, code: String, manager: NeoResourceManager): String
}