package net.typho.big_shot_lib.impl.util

import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.typho.big_shot_lib.api.util.ResourceUtil
import java.util.function.Consumer

class ResourceUtilImpl : ResourceUtil {
    override fun createSimpleResourceReloader(reloader: Consumer<ResourceManager>): PreparableReloadListener {
        return ResourceManagerReloadListener { reloader.accept(it) }
    }
}