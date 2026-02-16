package net.typho.big_shot_lib.api.client.rendering.event

import net.minecraft.world.level.chunk.LevelChunk
import java.util.*

fun interface ClientChunkChangedEvent {
    fun invoke(old: LevelChunk?, new: LevelChunk?)

    companion object : ClientChunkChangedEvent {
        private val callbacks = LinkedList<ClientChunkChangedEvent>()

        @JvmStatic
        fun register(callback: ClientChunkChangedEvent) = callbacks.add(callback)

        override fun invoke(old: LevelChunk?, new: LevelChunk?) {
            callbacks.forEach { it.invoke(old, new) }
        }
    }
}