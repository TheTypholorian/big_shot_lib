package net.typho.big_shot_lib.impl.neoforge

import net.neoforged.fml.ModContainer
import net.neoforged.neoforgespi.language.IModInfo
import net.neoforged.neoforgespi.language.IModLanguageLoader
import net.neoforged.neoforgespi.language.ModFileScanData
import net.typho.big_shot_lib.api.BigShotLib

class NeoForgeModLanguageLoader : IModLanguageLoader {
    override fun name() = BigShotLib.MOD_ID

    override fun version() = "1.0.0"

    override fun loadMod(
        info: IModInfo,
        scan: ModFileScanData,
        layer: ModuleLayer
    ): ModContainer {
        if (info !is ModInfoImpl) {
            throw IllegalStateException("Not allowed to use the big shot lib IModLanguageLoader in a regular neoforge mod. Either use a different language loader or switch completely to big shot lib.")
        }

        return ModContainerImpl(info)
    }
}