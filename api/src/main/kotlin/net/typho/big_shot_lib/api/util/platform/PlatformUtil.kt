package net.typho.big_shot_lib.api.util.platform

import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import java.nio.file.Path

private val INSTANCE by lazy { IPlatformUtil::class.loadService() }

object PlatformUtil : IPlatformUtil by INSTANCE

interface IPlatformUtil {
    val loader: ModLoader
    val mods: Collection<ModContainer>
    val configPath: Path

    fun isDevEnv(): Boolean

    fun isClient(): Boolean

    fun getMod(id: String): ModContainer? {
        return mods.firstOrNull { it.id == id }
    }
}