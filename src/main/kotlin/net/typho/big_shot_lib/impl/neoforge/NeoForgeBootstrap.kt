package net.typho.big_shot_lib.impl.neoforge

import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.neoforgespi.earlywindow.GraphicsBootstrapper
import net.typho.big_shot_lib.api.AgentLoader
import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.common.annotation.Environment
import net.typho.big_shot_lib.impl.agent.InstrumentationInit

class NeoForgeBootstrap : GraphicsBootstrapper {
    override fun name() = BigShotLib.MOD_ID

    override fun bootstrap(arguments: Array<String>) {
        AgentLoader.loadAgent(javaClass, "agent.jar")
        InstrumentationInit.init(AgentLoader.INSTRUMENTATION, when (FMLLoader.getCurrent().dist) {
            Dist.CLIENT -> Environment.CLIENT
            Dist.DEDICATED_SERVER -> Environment.SERVER
        })
    }
}