package net.typho.big_shot_lib.impl.neoforge

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.neoforged.fml.jarcontents.JarContents
import net.neoforged.fml.loading.moddiscovery.ModFile
import net.neoforged.fml.loading.moddiscovery.ModJarMetadata
import net.neoforged.neoforgespi.language.IConfigurable
import net.neoforged.neoforgespi.language.IModFileInfo
import net.neoforged.neoforgespi.language.IModInfo
import net.neoforged.neoforgespi.locating.IModFile
import net.neoforged.neoforgespi.locating.IModFileReader
import net.neoforged.neoforgespi.locating.ModFileDiscoveryAttributes
import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.common.loading.BigShotModInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NeoForgeModFileReader : IModFileReader {
    companion object {
        @JvmField
        val LOGGER = LoggerFactory.getLogger("Big Shot Mod Loader")
        
        @JvmStatic
        fun modJsonParser(file: IModFile): IModFileInfo? {
            val json = file.contents.get(BigShotModInfo.FILE_NAME)
            
            if (json == null) {
                LOGGER.warn("Mod ${file.filePath} is missing a ${BigShotModInfo.FILE_NAME} file")
                return null
            }

            return json.open().use { ModFileInfoImpl(file, JsonParser.parseReader(it.reader()).asJsonObject) }
        }
    }

    override fun read(
        jar: JarContents,
        attributes: ModFileDiscoveryAttributes
    ): IModFile? {
        return if (jar.containsFile(BigShotModInfo.FILE_NAME)) {
            val metadata = ModJarMetadata()
            ModFile(jar, metadata, ::modJsonParser, attributes.withReader(this)).also { metadata.setModFile(it) }
        } else null

        /*
        return jar.openFile("big_shot.mod.json")?.let { json ->
            try {
                val attributes = attributes.withReader(this)
                val json = JsonParser.parseReader(json.reader()).asJsonObject
            } catch (e: Exception) {
                throw IllegalStateException("Unable to load Big Shot Lib mod ${jar.primaryPath}. Please report this to the mod author.", e)
            }

            TODO()
        }
         */
    }

    class ModFileInfoImpl(
        private val file: IModFile,
        json: JsonObject
    ) : IModFileInfo {
        override fun getMods(): List<IModInfo> {
            TODO("Not yet implemented")
        }

        override fun requiredLanguageLoaders(): List<IModFileInfo.LanguageSpec> {
            TODO("Not yet implemented")
        }

        override fun showAsResourcePack() = false

        override fun showAsDataPack() = false

        override fun getFileProperties(): Map<String, Any> {
            TODO("Not yet implemented")
        }

        override fun getLicense(): String {
            TODO("Not yet implemented")
        }

        override fun versionString(): String {
            TODO("Not yet implemented")
        }

        override fun usesServices(): List<String> {
            TODO("Not yet implemented")
        }

        override fun getFile() = file

        override fun getConfig(): IConfigurable {
            TODO("Not yet implemented")
        }
    }
}