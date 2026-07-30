package net.typho.big_shot_lib.api.util.platform

import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.common.annotation.Environment
import java.nio.file.Path

private val INSTANCE by lazy { INeoModLoader::class.loadService() }

object NeoModLoader : INeoModLoader by INSTANCE

interface INeoModLoader {
    val backend: ModLoader
    val mods: Collection<ModContainer>
    val configPath: Path
    val environment: Environment

    fun isDevEnv(): Boolean

    fun getMod(id: String): ModContainer? {
        return mods.firstOrNull { it.id == id }
    }
}