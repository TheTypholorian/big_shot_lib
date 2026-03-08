package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface DebugScreenFactory {
    fun register(
        location: ResourceIdentifier,
        allowedWithReducedDebugInfo: Boolean,
        text: (out: (line: String) -> Unit) -> Unit
    )
}