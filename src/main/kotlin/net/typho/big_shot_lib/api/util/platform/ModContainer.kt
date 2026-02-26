package net.typho.big_shot_lib.api.util.platform

interface ModContainer {
    val modId: String
    val displayName: String
    val description: String
    val version: String
    val customData: Map<String, Any?>
}