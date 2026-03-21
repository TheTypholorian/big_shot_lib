package net.typho.big_shot_lib.impl.client.util

import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.event.ClientEventFactory
import net.typho.big_shot_lib.api.client.util.event.ClientLevelChangedEvent
import net.typho.big_shot_lib.api.client.util.event.RenderEvent
import net.typho.big_shot_lib.api.client.util.event.WindowResizeEvent

object BigShotClientEvents : ClientEventFactory {
    override val frameStart: MutableList<Runnable> = arrayListOf()
    override val frameEnd: MutableList<Runnable> = arrayListOf()
    override val levelRenderEnd: MutableList<RenderEvent> = arrayListOf()
    override val windowResized: MutableList<WindowResizeEvent> = arrayListOf()
    override val levelChanged: MutableList<ClientLevelChangedEvent> = arrayListOf()

    init {
        BigShotClientEntrypoint.registerEvents(this)

        //? fabric {
        //? } neoforge {
        //? }
    }
}