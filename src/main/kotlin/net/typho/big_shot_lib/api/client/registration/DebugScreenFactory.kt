package net.typho.big_shot_lib.api.client.registration

import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.function.Consumer

interface DebugScreenFactory {
    fun register(
        id: ResourceIdentifier,
        allowedWithReducedDebugInfo: Boolean,
        out: Consumer<Consumer<String>>
    )
}