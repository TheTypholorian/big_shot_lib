package net.typho.big_shot_lib.api.util.platform

import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface ModContainer {
    val modId: String
    val displayName: String
    val description: String
    val version: String
    val customData: Map<String, Any?>

    fun <T> loadEntrypoint(id: ResourceIdentifier, cls: Class<T>): T?
}