package net.typho.big_shot_lib.api.render_queue

import net.minecraft.client.Camera
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.world.level.Level

interface RenderEvent {
    fun render(
        minecraft: Minecraft,
        level: Level,
        deltaTracker: DeltaTracker,
        camera: Camera
    )
}