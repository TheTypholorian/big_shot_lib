package net.typho.big_shot_lib.api.client.event

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.client.Camera
import net.minecraft.client.DeltaTracker
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.typho.big_shot_lib.api.client.rendering.util.RenderLevelStage
import org.joml.FrustumIntersection
import org.joml.Matrix4f

fun interface ClientCommandsEvent {
    fun registerClientCommands(
        dispatcher: CommandDispatcher<*>, // TODO
        context: CommandBuildContext
    )
}