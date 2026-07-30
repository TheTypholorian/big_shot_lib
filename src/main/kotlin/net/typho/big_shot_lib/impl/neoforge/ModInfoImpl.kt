package net.typho.big_shot_lib.impl.neoforge

import net.neoforged.neoforgespi.language.IConfigurable
import net.neoforged.neoforgespi.language.IModFileInfo
import net.neoforged.neoforgespi.language.IModInfo
import net.neoforged.neoforgespi.language.IModLanguageLoader
import net.neoforged.neoforgespi.locating.ForgeFeature
import org.apache.maven.artifact.versioning.ArtifactVersion
import java.net.URL
import java.util.Optional

class ModInfoImpl(
    private val owner: ModFileInfoImpl
) : IModInfo {
    override fun getOwningFile() = owner

    override fun getLoader(): IModLanguageLoader? {
        TODO("Not yet implemented")
    }

    override fun getModId(): String? {
        TODO("Not yet implemented")
    }

    override fun getDisplayName(): String? {
        TODO("Not yet implemented")
    }

    override fun getDescription(): String? {
        TODO("Not yet implemented")
    }

    override fun getVersion(): ArtifactVersion? {
        TODO("Not yet implemented")
    }

    override fun getDependencies(): List<IModInfo.ModVersion?>? {
        TODO("Not yet implemented")
    }

    override fun getForgeFeatures(): List<ForgeFeature.Bound?>? {
        TODO("Not yet implemented")
    }

    override fun getNamespace(): String? {
        TODO("Not yet implemented")
    }

    override fun getModProperties(): Map<String?, Any?>? {
        TODO("Not yet implemented")
    }

    override fun getUpdateURL(): Optional<URL?>? {
        TODO("Not yet implemented")
    }

    override fun getModURL(): Optional<URL?>? {
        TODO("Not yet implemented")
    }

    override fun getLogoFile(): Optional<String?>? {
        TODO("Not yet implemented")
    }

    override fun getLogoBlur(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getConfig(): IConfigurable? {
        TODO("Not yet implemented")
    }
}