package net.typho.big_shot_lib.api.event

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

fun interface CommandsEvent {
    fun registerCommonCommands(
        dispatcher: CommandDispatcher<CommandSourceStack>,
        environment: Commands.CommandSelection,
        context: CommandBuildContext
    )
}