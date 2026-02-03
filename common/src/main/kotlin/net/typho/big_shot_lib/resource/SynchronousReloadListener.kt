package net.typho.big_shot_lib.resource

import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.Unit
import net.minecraft.util.profiling.Profiler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

interface SynchronousReloadListener : PreparableReloadListener {
    override fun reload(
        barrier: PreparableReloadListener.PreparationBarrier,
        manager: ResourceManager,
        gameExecutor: Executor,
        p3: Executor
    ): CompletableFuture<Void> {
        return barrier.wait<Unit?>(Unit.INSTANCE).thenRunAsync({
            val profilerFiller = Profiler.get()
            profilerFiller.push("listener")
            reload(manager)
            profilerFiller.pop()
        }, gameExecutor)
    }

    fun reload(manager: ResourceManager)
}