package net.typho.big_shot_lib.api.client.event

import net.typho.big_shot_lib.api.util.resource.NamedResource
import java.util.function.Consumer

interface DebugScreenEntry : NamedResource {
    val allowedWithReducedDebugInfo: Boolean

    fun accept(out: Consumer<String>)
}