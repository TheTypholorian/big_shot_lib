package net.typho.big_shot_lib.api.util.platform

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.util.content.RegisteredObject
import java.nio.file.Path

interface PlatformUtil {
    val loader: ModLoader
    val mods: Collection<ModContainer>
    val configPath: Path

    fun isDevEnv(): Boolean

    fun isClient(): Boolean

    fun getMod(id: String): ModContainer? {
        return mods.firstOrNull { it.id == id }
    }

    fun <T : Any> createRegisteredObject(
        location: Identifier,
        constructor: () -> T
    ): RegisteredObject<T>

    companion object {
        @JvmStatic
        @get:JvmName("getInstance")
        val INSTANCE by lazy { PlatformUtil::class.loadService() }
    }
}