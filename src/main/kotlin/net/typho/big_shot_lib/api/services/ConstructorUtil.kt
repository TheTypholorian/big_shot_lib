package net.typho.big_shot_lib.api.services

import net.minecraft.server.packs.resources.ResourceManager
import java.util.*

interface ConstructorUtil {
    fun wrap(manager: ResourceManager): ResourceManagerWrapper

    companion object {
        @JvmField
        val INSTANCE: ConstructorUtil = ServiceLoader.load(ConstructorUtil::class.java).findFirst().orElseThrow()
    }
}