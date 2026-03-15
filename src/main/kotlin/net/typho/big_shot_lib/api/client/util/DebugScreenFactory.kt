package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface DebugScreenFactory {
    fun register(
        location: NeoIdentifier,
        allowedWithReducedDebugInfo: Boolean,
        text: (out: (line: String) -> Unit) -> Unit
    )
}