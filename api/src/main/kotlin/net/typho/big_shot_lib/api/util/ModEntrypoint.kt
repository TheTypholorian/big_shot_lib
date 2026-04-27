package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.util.platform.ModContainer
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

abstract class ModEntrypoint {
    val metadata = ModContainer.get(javaClass) ?: throw NullPointerException("Could not find big shot lib mod metadata for ${javaClass.name}")

    fun id(path: String) = NeoIdentifier(metadata.modId, path)
}