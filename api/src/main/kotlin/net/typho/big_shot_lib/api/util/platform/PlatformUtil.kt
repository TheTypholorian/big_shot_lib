package net.typho.big_shot_lib.api.util.platform

import net.typho.big_shot_lib.api.client.util.Registrar
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import java.nio.file.Path

interface PlatformUtil {
    val loader: ModLoader
    val mods: Collection<ModContainer>
    val configPath: Path

    fun isDevEnv(): Boolean

    fun createRegistrar(): Registrar

    companion object {
        val INSTANCE by lazy { PlatformUtil::class.loadService() }
    }
}