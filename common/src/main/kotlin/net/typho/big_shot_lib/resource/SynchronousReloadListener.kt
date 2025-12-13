package net.typho.big_shot_lib.resource

import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.Unit
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

interface SynchronousReloadListener : PreparableReloadListener {
    override fun reload(
        preparationBarrier: PreparableReloadListener.PreparationBarrier,
        resourceManager: ResourceManager,
        preparationsProfiler: ProfilerFiller,
        reloadProfiler: ProfilerFiller,
        backgroundExecutor: Executor,
        gameExecutor: Executor
    ): CompletableFuture<Void?> {
        return preparationBarrier.wait(Unit.INSTANCE).thenRunAsync({
            reloadProfiler.startTick()
            reloadProfiler.push("listener")
            reload(resourceManager)
            reloadProfiler.pop()
            reloadProfiler.endTick()
        }, gameExecutor)
    }

    fun reload(manager: ResourceManager)
}