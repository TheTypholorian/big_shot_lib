package net.typho.big_shot_lib.api.util

import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import java.util.*
import java.util.function.Consumer

interface ResourceUtil {
    fun createSimpleResourceReloader(reloader: Consumer<ResourceManager>): PreparableReloadListener

    companion object {
        @JvmField
        val INSTANCE: ResourceUtil = ServiceLoader.load(ResourceUtil::class.java).findFirst().orElseThrow()
    }
}