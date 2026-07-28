package net.typho.big_shot_lib.impl.neoforge

import net.neoforged.neoforgespi.earlywindow.GraphicsBootstrapper
import net.typho.big_shot_lib.api.AgentLoader
import net.typho.big_shot_lib.api.BigShotLib

class NeoForgeBootstrap : GraphicsBootstrapper {
    override fun name() = BigShotLib.MOD_ID

    override fun bootstrap(arguments: Array<String>) {
        AgentLoader.loadAgent(javaClass, "agent.jar")
    }
}