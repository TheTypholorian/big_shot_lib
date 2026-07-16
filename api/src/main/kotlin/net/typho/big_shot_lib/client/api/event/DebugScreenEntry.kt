package net.typho.big_shot_lib.client.api.event

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.util.resource.NamedResource
import java.util.function.Consumer

interface DebugScreenEntry : NamedResource {
    val allowedWithReducedDebugInfo: Boolean

    fun accept(out: Consumer<String>)

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke(
            location: Identifier,
            allowedWithReducedDebugInfo: Boolean,
            text: Consumer<Consumer<String>>
        ) = object : DebugScreenEntry {
            override val location: Identifier = location
            override val allowedWithReducedDebugInfo: Boolean = allowedWithReducedDebugInfo

            override fun accept(out: Consumer<String>) {
                text.accept(out)
            }
        }
    }
}