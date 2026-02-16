package net.typho.big_shot_lib.api.client.rendering.event

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.*

fun interface BlockChangedEvent {
    fun invoke(level: Level, pos: BlockPos, old: BlockState?, new: BlockState?)

    companion object : BlockChangedEvent {
        private val callbacks = LinkedList<BlockChangedEvent>()

        @JvmStatic
        fun register(callback: BlockChangedEvent) = callbacks.add(callback)

        override fun invoke(level: Level, pos: BlockPos, old: BlockState?, new: BlockState?) {
            callbacks.forEach { it.invoke(level, pos, old, new) }
        }
    }
}